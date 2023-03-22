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
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_t")
public class User {

    @Id
    private String id;

    @Column(name = "username", nullable = false, updatable = false)
    private String username;

    @Column(name = "email", nullable = false, updatable = false)
    private String email;

    @Column(name = "balance", nullable = false)
    private Double balance;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Review> reviews;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Purchase> buyHistory;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Notification> notifications;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Organization> organizations;

    @Enumerated(EnumType.STRING)
    private ActiveStatus status;
}
