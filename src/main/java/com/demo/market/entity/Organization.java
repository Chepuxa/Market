package com.demo.market.entity;

import com.demo.market.enums.ActiveStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "organization_t")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "org_seq")
    @SequenceGenerator(name = "prg_seq", sequenceName = "ORG_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false, updatable = false)
    private String name;

    @Column(name = "description", updatable = false)
    private String description;

    @Column(name = "logotype", updatable = false)
    private String logotype;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organization")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Product> products;

    @Enumerated(EnumType.STRING)
    private ActiveStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
}
