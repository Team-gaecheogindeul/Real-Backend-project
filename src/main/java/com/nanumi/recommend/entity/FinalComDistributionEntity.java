package com.nanumi.recommend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class FinalComDistributionEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name="view_cnt_study") // 1번 카테고리 공부
    protected Float view_cnt_study;

    @Column(name="view_cnt_daily") // 2번 카테고리 일상
    protected Float view_cnt_daily;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq",referencedColumnName = "user_seq")
    private UserComViewLogEntity user;
}
