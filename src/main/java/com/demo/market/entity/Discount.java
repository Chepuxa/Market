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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "discount_t")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dsc_seq")
    @SequenceGenerator(name = "dsc_seq", sequenceName = "DSC_SEQ", allocationSize = 1)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "discount_products",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "discount_id")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Product> products;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @Column(nullable = false)
    private Timestamp endTime;
}
