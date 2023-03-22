package com.demo.market.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "review_t")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rvw_seq")
    @SequenceGenerator(name = "rvw_seq", sequenceName = "RVW_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "mark", nullable = false)
    private Double mark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
}
