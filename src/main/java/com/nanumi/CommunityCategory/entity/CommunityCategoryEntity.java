package com.nanumi.CommunityCategory.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "chat_board")
public class CommunityCategoryEntity {
    @Id
    @Column(name = "post_id")
    private Long id; //게시글 아이디

    @Column(name= "content")
    private String content;

    @Column(name = "new_category1")
    private String new_category1;

    @Column(name = "new_category2")
    private String new_category2;
}
