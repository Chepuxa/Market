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
@Table(name = "notification_t")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ntf_seq")
    @SequenceGenerator(name = "ntf_seq", sequenceName = "ntf_seq", allocationSize = 1)
    private Long id;

    @Column(name = "header", nullable = false, updatable = false)
    private String header;

    @Column(name = "content", nullable = false, updatable = false)
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @Column(nullable = false)
    private Timestamp timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
}
