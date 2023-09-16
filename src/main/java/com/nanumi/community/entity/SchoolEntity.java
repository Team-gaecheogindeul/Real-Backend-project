package com.nanumi.community.entity;

import com.nanumi.community.dto.CommunityDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
public class SchoolEntity extends CommunityEntity{
    //[DTO 객체 -> Entity 객체 변환]
    public static SchoolEntity toSaveSchoolEntity(CommunityDTO communityDTO) {
        SchoolEntity schoolEntity = new SchoolEntity();

        schoolEntity.setId(communityDTO.getBoard_id());
        schoolEntity.setBoard_title(communityDTO.getBoard_title());
        schoolEntity.setUser_seq(communityDTO.getUser_seq());
        schoolEntity.setCategory_id(communityDTO.getCategory_id());
        schoolEntity.setBoard_story(communityDTO.getBoard_story());
        schoolEntity.setUserGrade(communityDTO.getUserGrade());
        schoolEntity.setNickName(communityDTO.getNickName());
        schoolEntity.setDate(communityDTO.getDate());
        schoolEntity.setLikeCount(communityDTO.getLikeCount());
        schoolEntity.setUserImageUrl(communityDTO.getUserImageUrl());
        schoolEntity.setBoardImages(new ArrayList<>(communityDTO.getBoardImageUrls()));
        /*
        이 코드는 CommunityDTO 객체의 '이미지 URL 문자열 리스트(boardImageUrls)' 를 가져와서
        이를 CommunityEntity 객체의 '이미지 Entity 리스트(boardImages)' 에 설정하는 부분입니다.
         */
//        if (communityDTO.getBoardImageUrls() != null) { //CommunityDTO 객체의 boardImageUrls 필드가 null 이 아닌지 확인
//            List<ImageEntity> imageEntities = communityDTO.getBoardImageUrls().stream() // String 리스트(boardImageUrls)를 Stream 으로 변환
//                    //.map 함수 : 스트림의 각 요소에 대해 주어진 함수를 적용하고 그 결과로 구성된 새로운 스트림을 반환
//                    .map(url -> {
//                        ImageSchoolEntity image = new ImageSchoolEntity();
//                        image.setBoardImageUrl(url);
//                        return image;
//                    }) // 각 URL을 사용하여 ImageEntity 객체를 생성
//                    .collect(Collectors.toList());
//            schoolEntity.setBoardImages(imageEntities);
//        } else {
//            schoolEntity.setBoardImages(Collections.emptyList());
//        }
        return schoolEntity;
    }
}
