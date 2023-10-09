package com.nanumi.recommend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public class UserComViewLogEntity {
    @Id
    @Column(name="user_seq")
    private String user_seq;

    @Column(name="view_cnt_study") // 1번 카테고리 공부
    protected Float view_cnt_study;

    @Column(name="view_cnt_daily") // 2번 카테고리 일상
    protected Float view_cnt_daily;
}
