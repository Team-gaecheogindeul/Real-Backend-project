package com.nanumi.recommend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public class UserViewLogEntity {
    @Id
    @Column(name="user_seq")
    private String user_seq;

    @Column(name="view_cnt_category1") // 1번 카테고리 코딩
    private Float view_cnt_category1;

    @Column(name="view_cnt_category2") // 2번 카테고리 소설
    private Float view_cnt_category2;

}

