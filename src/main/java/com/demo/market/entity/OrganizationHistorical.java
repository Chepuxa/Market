package com.demo.market.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "organization_historical_t")
public class OrganizationHistorical {

    @Id
    private Long id;

    @Column(name = "name", nullable = false, updatable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "logotype")
    private String logotype;

    @Column(name = "product_id", nullable = false, updatable = false)
    private String userId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organization")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Purchase> purchases;
}
