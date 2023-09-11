package com.nanumi.community.entity;

import com.nanumi.community.dto.CommunityDTO;
import com.nanumi.community.entity.ImageEntity.ImageEntity;
import com.nanumi.community.entity.ImageEntity.ImageFreeEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        /*
        이 코드는 CommunityDTO 객체의 '이미지 URL 문자열 리스트(boardImageUrls)' 를 가져와서
        이를 CommunityEntity 객체의 '이미지 Entity 리스트(boardImages)' 에 설정하는 부분입니다.
         */
        if (communityDTO.getBoardImageUrls() != null) { //CommunityDTO 객체의 boardImageUrls 필드가 null 이 아닌지 확인
            List<ImageEntity> imageEntities = communityDTO.getBoardImageUrls().stream() // String 리스트(boardImageUrls)를 Stream 으로 변환
                    //.map 함수 : 스트림의 각 요소에 대해 주어진 함수를 적용하고 그 결과로 구성된 새로운 스트림을 반환
                    .map(url -> {
                        ImageFreeEntity image = new ImageFreeEntity();
                        image.setBoardImageUrl(url);
                        return image;
                    }) // 각 URL을 사용하여 ImageEntity 객체를 생성
                    .collect(Collectors.toList());
            freeEntity.setBoardImages(imageEntities);
        } else {
            freeEntity.setBoardImages(Collections.emptyList());
        }
        return freeEntity;
    }
}
