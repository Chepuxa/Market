package com.demo.market.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "purchase_t")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prc_seq")
    @SequenceGenerator(name = "prc_seq", sequenceName = "PRC_SEQ", allocationSize = 1)
    @Column(name =  "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "product_id", nullable = false, updatable = false)
    private Long productId;

    @Column(name = "name", nullable = false, updatable = false)
    private String name;

    @Column(name = "description", nullable = false, updatable = false)
    private String description;

    @Column(name = "price", nullable = false, updatable = false)
    private Double priceWithDiscount;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @Column(nullable = false)
    private Timestamp buyTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private OrganizationHistorical organization;
}
