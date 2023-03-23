package com.demo.market.service.product;

import com.demo.market.dto.Auth;
import com.demo.market.dto.product.ProductRequest;
import com.demo.market.dto.product.ProductResponse;
import com.demo.market.dto.purchase.PurchaseResponse;
import com.demo.market.entity.Discount;
import com.demo.market.entity.Organization;
import com.demo.market.entity.OrganizationHistorical;
import com.demo.market.entity.Product;
import com.demo.market.entity.Purchase;
import com.demo.market.entity.User;
import com.demo.market.enums.ActiveStatus;
import com.demo.market.enums.Role;
import com.demo.market.enums.Type;
import com.demo.market.exceptions.InsufficientRights;
import com.demo.market.exceptions.ItemNotFound;
import com.demo.market.exceptions.NotEnoughCredits;
import com.demo.market.exceptions.OutdatedDiscount;
import com.demo.market.mappers.ProductMapper;
import com.demo.market.mappers.PurchaseMapper;
import com.demo.market.repository.DiscountRepository;
import com.demo.market.repository.OrganizationHistoricalRepository;
import com.demo.market.repository.OrganizationRepository;
import com.demo.market.repository.ProductRepository;
import com.demo.market.repository.PurchaseRepository;
import com.demo.market.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    DiscountRepository discountRepository;

    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    OrganizationHistoricalRepository organizationHistoricalRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    PurchaseMapper purchaseMapper;

    @Value("${app.service.commission}")
    private Long commission;

    @Override
    public PurchaseResponse buy(Auth auth, Long productId, Optional<Long> discountId) {
        User user = userRepository.findByIdAndStatus(auth.getUserId(), ActiveStatus.ACTIVE)
                .orElseThrow(InsufficientRights::new);
        return productRepository.findByIdAndStatus(productId, ActiveStatus.ACTIVE)
                .map(prd -> {
                    if (prd.getOrganization().getUser().getId().equals(user.getId())) {
                        throw new InsufficientRights();
                    }
                    if (prd.getAmount() == 0) {
                        throw new ItemNotFound(Type.PRODUCT);
                    }
                    Double price = prd.getPrice();
                    Long discountAmount = discountId
                            .map(dscId -> {
                                Optional<Discount> discount = discountRepository.findById(dscId);
                                return discount
                                        .map(dsc -> {
                                            if (prd.getDiscounts().stream().noneMatch(d -> d.getId().equals(dsc.getId()))) {
                                                throw new ItemNotFound(Type.DISCOUNT);
                                            }
                                            if (dsc.getEndTime().before(Timestamp.from(Instant.now()))) {
                                                throw new OutdatedDiscount();
                                            }
                                            return dsc.getAmount();
                                        })
                                        .orElseThrow(() -> {
                                            throw new ItemNotFound(Type.DISCOUNT);
                                        });
                            })
                            .orElse(0L);
                    price = calculateCut(discountAmount, price);
                    if (user.getBalance() < price) {
                        throw new NotEnoughCredits();
                    }
                    user.setBalance(user.getBalance() - calculateCut(commission, price));
                    Double newHolderBalance = prd.getOrganization().getUser().getBalance() + price;
                    prd.getOrganization().getUser().setBalance(newHolderBalance);
                    prd.setAmount(prd.getAmount() - 1);
                    productRepository.save(prd);
                    OrganizationHistorical organizationHistorical = new OrganizationHistorical();
                    organizationHistorical.setName(prd.getOrganization().getName());
                    organizationHistorical.setDescription(prd.getOrganization().getDescription());
                    organizationHistorical.setLogotype(prd.getOrganization().getLogotype());
                    organizationHistorical.setUserId(prd.getOrganization().getUser().getId());
                    organizationHistorical.setId(prd.getId());
                    organizationHistoricalRepository.save(organizationHistorical);
                    Purchase purchase = productMapper.productToPurchase(prd);
                    purchase.setPriceWithDiscount(price);
                    purchase.setUser(user);
                    purchase.setProductId(productId);
                    purchase.setOrganization(organizationHistorical);
                    purchase.setBuyTime(Timestamp.from(Instant.now()));
                    return purchaseMapper.toDto(purchaseRepository.save(purchase));
                })
                .orElseThrow(() -> new ItemNotFound(Type.PRODUCT));
    }

    private Double calculateCut(Long discount, Double price) {
        return price - (price * ((double) discount / 100));
    }

    @Override
    public ProductResponse add(Auth auth, ProductRequest productRequest) {
        User user = userRepository.findByIdAndStatus(auth.getUserId(), ActiveStatus.ACTIVE)
                .orElseThrow(InsufficientRights::new);
        return organizationRepository.findByIdAndStatus(productRequest.getOrganizationId(), ActiveStatus.ACTIVE)
                .map(org -> {
                    if (!Objects.equals(org.getUser().getId(), user.getId())) {
                        throw new InsufficientRights();
                    }
                    Product product = productMapper.toEntity(productRequest);
                    product.setStatus(ActiveStatus.ON_HOLD);
                    product.setOrganization(org);
                    return productMapper.toDto(productRepository.save(product));
                })
                .orElseThrow(() -> new ItemNotFound(Type.ORGANIZATION));
    }

    @Override
    public ProductResponse update(Auth auth, Long productId, ProductRequest productRequest) {
        Product product;
        if (!auth.getUserRoles().contains(Role.ADMIN.withPrefix())) {
            product = productRepository.findById(productId).orElseThrow(() -> new ItemNotFound(Type.PRODUCT));
            userRepository.findByIdAndStatus(auth.getUserId(), ActiveStatus.ACTIVE)
                    .orElseThrow(InsufficientRights::new);
            if (!Objects.equals(product.getOrganization().getUser().getId(), auth.getUserId())) {
                throw new InsufficientRights();
            }
            if (!Objects.equals(product.getOrganization().getId(), productRequest.getOrganizationId())) {
                throw new InsufficientRights();
            }
        } else {
            product = productRepository.findById(productId).orElseThrow(() -> new ItemNotFound(Type.PRODUCT));
        }
        Product newProduct = productMapper.toEntity(productRequest);
        if (!Objects.equals(product.getOrganization().getId(), productRequest.getOrganizationId())) {
            Organization organization = organizationRepository.findById(productRequest.getOrganizationId())
                    .orElseThrow(() -> new ItemNotFound(Type.ORGANIZATION));
            newProduct.setOrganization(organization);
        }
        return productMapper.toDto(productRepository.save(newProduct));
    }

    @Override
    public ProductResponse get(Auth auth, Long productId) {
        if (!auth.getUserRoles().contains(Role.ADMIN.withPrefix())) {
            userRepository.findByIdAndStatus(auth.getUserId(), ActiveStatus.ACTIVE).orElseThrow(InsufficientRights::new);
        }
        return productRepository.findByIdAndStatus(productId, ActiveStatus.ACTIVE)
                .map(productMapper::toDto)
                .orElseThrow(() -> new ItemNotFound(Type.PRODUCT));
    }

    @Override
    public Set<ProductResponse> getAll(Auth auth) {
        if (!auth.getUserRoles().contains(Role.ADMIN.withPrefix())) {
            userRepository.findByIdAndStatus(auth.getUserId(), ActiveStatus.ACTIVE).orElseThrow(InsufficientRights::new);
        }
        Set<Product> products = productRepository.findAllByStatus(ActiveStatus.ACTIVE);
        return productMapper.toDtoSet(products);
    }

    @Override
    public ProductResponse delete(Long productId) {
        return productRepository.findById(productId)
                .map(prd -> {
                    ProductResponse response = productMapper.toDto(prd);
                    productRepository.delete(prd);
                    return response;
                })
                .orElseThrow(() -> new ItemNotFound(Type.PRODUCT));
    }

    @Override
    public ProductResponse activate(Long productId) {
        return productRepository.findById(productId)
                .map(prd -> {
                    if (prd.getStatus().equals(ActiveStatus.ON_HOLD) || prd.getStatus().equals(ActiveStatus.INACTIVE)) {
                        prd.setStatus(ActiveStatus.ACTIVE);
                    } else {
                        prd.setStatus(ActiveStatus.INACTIVE);
                    }
                    return productMapper.toDto(productRepository.save(prd));
                })
                .orElseThrow(() -> new ItemNotFound(Type.PRODUCT));
    }
}
