package com.nanumi.community.entity;

import com.nanumi.community.dto.CommunityDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
@Getter
@Setter
public class FreeEntity extends CommunityEntity {

    //[DTO 객체 -> Entity 객체 변환]
    public static FreeEntity toSaveFreeEntity(CommunityDTO communityDTO) {
        FreeEntity freeEntity = new FreeEntity();

        freeEntity.setId(communityDTO.getBoard_id());
        freeEntity.setBoard_title(communityDTO.getBoard_title());
        freeEntity.setUser_seq(communityDTO.getUser_seq());
        freeEntity.setCategory_id(communityDTO.getCategory_id());
        freeEntity.setBoard_story(communityDTO.getBoard_story());
        freeEntity.setUserGrade(communityDTO.getUserGrade());
        freeEntity.setNickName(communityDTO.getNickName());
        freeEntity.setDate(communityDTO.getDate());
        freeEntity.setLikeCount(communityDTO.getLikeCount());
        freeEntity.setUserImageUrl(communityDTO.getUserImageUrl());
        freeEntity.setBoardImages(new ArrayList<>(communityDTO.getBoardImageUrls()));
        return freeEntity;
    }
}
