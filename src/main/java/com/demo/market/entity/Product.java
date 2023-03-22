package com.demo.market.entity;

import com.demo.market.enums.ActiveStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "product_t")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prd_seq")
    @SequenceGenerator(name = "prd_seq", sequenceName = "PRD_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Organization organization;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "products")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Discount> discounts;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @Column(name = "reviews")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Review> reviews;

    @Column(name = "tags")
    private String tags;

    @Column(name = "characteristics", nullable = false)
    private String characteristics;

    @Enumerated(EnumType.STRING)
    private ActiveStatus status;

    @Column(name = "rating")
    @Formula("(SELECT AVG(e.mark) FROM review_t as e WHERE e.product_id = id)")
    private Double rating = 0.0;
}
