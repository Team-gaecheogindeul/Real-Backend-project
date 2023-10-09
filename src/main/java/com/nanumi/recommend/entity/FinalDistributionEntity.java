package com.nanumi.recommend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class FinalDistributionEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name="view_cnt_category1") // 1번 카테고리 코딩
    protected Float view_cnt_category1;

    @Column(name="view_cnt_category2") // 2번 카테고리 소설
    protected Float view_cnt_category2;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq",referencedColumnName = "user_seq")
    private UserViewLogEntity user;
}
