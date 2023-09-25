package com.nanumi.community.service;

import com.nanumi.board_give.DataNotFoundException.DataNotFoundException;
import com.nanumi.community.dto.CommentDTO;
import com.nanumi.community.dto.CommunityDTO;
import com.nanumi.community.entity.CollegeEntity;
import com.nanumi.community.entity.CommentEntity.CommentEntity;
import com.nanumi.community.entity.UsersLikesEntity.CommunityLikesEntity;
import com.nanumi.community.repository.CollegeRepository;
import com.nanumi.community.repository.CommentRepository;
import com.nanumi.community.repository.UsersLikesCommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollegeService {
    private final CollegeRepository collegeRepository;
    private final UsersLikesCommunityRepository usersLikesCommunityRepository;
    private final CommentRepository commentRepository;

    //[#1. 대학교입시 게시글 등록 ]
    public void PostingSave(CommunityDTO communityDTO) {

        CollegeEntity collegeEntity = CollegeEntity.toSaveCollegeEntity(communityDTO); //DTO -> Entity 변환
        collegeRepository.save(collegeEntity);
    }



//------------------------------------------------------------------------------------------------------------------


    //[#2. 게시글 전체 조회]
    @Transactional
    public Page<CommunityDTO> postingFindAll(Pageable pageable) {
        Page<CollegeEntity> collegeEntityList = collegeRepository.findAll(pageable); //리포지토리에서 데이터들은 List 형태의 Entity 가 넘어오게 된다.

        // Entity -> Dto 로 옮겨 닮아서, controller 로 반환해줘야 한다.
        return new PageImpl<>(collegeEntityList.stream().map(CommunityDTO::toCommunityDTO).collect(Collectors.toList()), pageable, collegeEntityList.getTotalElements());
    }

    //-------------------------------------------------------------------------------------------------------

    //[#3.  게시글 상세 조회]
    @Transactional
    public Optional<CommunityDTO> postingFindById(Long board_id) {
        Optional<CollegeEntity> collegeEntity = collegeRepository.findById(board_id);
        return collegeEntity.map(CommunityDTO::toCommunityDTO); //map() 메소드를 사용하여, Optional<BoardEntity> 내부의 BoardEntity 객체가 존재할 경우에만 BoardDTO.toBoardDTO() 메소드를 호출하여 Optional<BoardDTO> 객체로 변환하였습니다.
    }


    //-------------------------------------------------------------------------------------------------------

    //[#4. (개인) 게시글 전체 조회] - BoardRepository 사용
    @Transactional
    public Page<CommunityDTO> findAllPostingsByUserSeq(String user_seq, Pageable pageable) {
        Page<CollegeEntity> collegeEntityPage = collegeRepository.findAllByUserSeq(user_seq, pageable); // Pageable 객체 추가
        return collegeEntityPage.map(CommunityDTO::toCommunityDTO); // 각 BoardEntity 객체를 BoardDTO로 변환한 후 페이지 형태로 반환
    }

    //-------------------------------------------------------------------------------------------------------

    //[#6. (개인) 게시글 수정]
    @Transactional
    public void updatePartOfBoard(Long board_id, CommunityDTO communityDTO) {
        //요청된 게시물의 '게시글 번호' 로 -----> 기존 게시물 '엔티티' 조회
        Optional<CollegeEntity> optionalCollegeEntity = collegeRepository.findById(board_id);

        if(optionalCollegeEntity.isPresent()) { // 게시글 엔티티가 존재시, 해당 데이터들을 모두 불러온다.
            CollegeEntity collegeEntity = optionalCollegeEntity.get();

            //#1. CommunityDTO 로부터 받은 값이 있는 경우 : 그 값(communityDTO.getBoard_title())을 freeEntity 에 설정
            //만약, CommunityDTO 로부터 값을 못 받은 경우 : 기존 값(freeEntity.getBoard_title()))을 freeEntity 에 설정
            collegeEntity.setBoard_title(Optional.ofNullable(communityDTO.getBoard_title()).orElse(collegeEntity.getBoard_title())); // 게시글 제목
            collegeEntity.setUser_seq(Optional.ofNullable(communityDTO.getUser_seq()).orElse(collegeEntity.getUser_seq())); // 회원 일련번호
            collegeEntity.setCategory_id(Optional.ofNullable(communityDTO.getCategory_id()).orElse(collegeEntity.getCategory_id())); //카테고리 아이디
            collegeEntity.setBoard_story(Optional.ofNullable(communityDTO.getBoard_story()).orElse(collegeEntity.getBoard_story())); // 게시글 내용
            collegeEntity.setUserGrade(Optional.ofNullable(communityDTO.getUserGrade()).orElse(collegeEntity.getUserGrade())); // 사용자 등급
            collegeEntity.setNickName(Optional.ofNullable(communityDTO.getNickName()).orElse(collegeEntity.getNickName())); // 사용자 닉네임
            collegeEntity.setDate(Optional.ofNullable(communityDTO.getDate()).orElse(collegeEntity.getDate())); // 게시글 작성 시간(날짜)
            collegeEntity.setUserImageUrl(Optional.ofNullable(communityDTO.getUserImageUrl()).orElse(collegeEntity.getUserImageUrl())); //사용자 프로필 이미지
            collegeEntity.setLikeCount(Optional.ofNullable(communityDTO.getLikeCount()).orElse(collegeEntity.getLikeCount())); // 좋아요 갯수
            collegeEntity.setId(collegeEntity.getId()); // 게시글 번호 (그대로 유지)
            // 추가 이미지들이 여러장 존재 시
            if (communityDTO.getBoardImageUrls() != null) {
                List<String> updatedImages = new ArrayList<>(collegeEntity.getBoardImages()); // 기존 이미지를 가지는 새로운 이미지 객체 생성
                updatedImages.addAll(communityDTO.getBoardImageUrls());
                collegeEntity.setBoardImages(updatedImages);
            } // 추가 이미지들이 없을 때
            collegeEntity.setBoardImages(collegeEntity.getBoardImages()); // 기존 이미지만 다시 세팅

            //#2.  변경된 게시물 엔티티 저장
            collegeRepository.save(collegeEntity);

        } else { //해당 ID의 엔티티(기존 게시글)가 존재하지 않는다면 NoSuchElementException 을 발생시켜, 이 메소드를 호출한 곳에서 이 예외를 처리할 수 있도록 합니다.
            throw new NoSuchElementException("No Board found with id: " + board_id);
        }
    }


//-------------------------------------------------------------------------------------------------------

    // [#7. 좋아요 누르기]

    @Transactional
    public void addLikeToPost(String user_seq, Long board_id) {

        // 1. 해당 게시글의 모든 데이터를 가져온다.
        CollegeEntity collegeEntity = collegeRepository.findById(board_id) // findById 메소드의 반환타입은 항상 Optional<T>
                .orElseThrow(() -> new DataNotFoundException("게시글이 존재하지 않습니다."));

        // 2. 현재 해당 게시글의 총 좋아요 갯수를 가져온다.
        Long currentLikeCount = Optional.ofNullable(collegeEntity.getLikeCount()).orElse(0L);

        // 2. 사용자 고유 번호를 사용해 usersLikesRepository 에서 해당 사용자가 좋아요를 누른 모든 게시글 id를 불러옴
        CommunityLikesEntity communityLikesEntity = usersLikesCommunityRepository.findByUserSeq(user_seq)
                .orElseGet(
                        () -> { // 조회 결과가 없으면 람다식을 사용하여 새로운 UsersLikesEntity 객체를 생성
                            CommunityLikesEntity newCommunityLikesEntity = new CommunityLikesEntity();
                            newCommunityLikesEntity.setUserSeq(user_seq);
                            return newCommunityLikesEntity;}
                );

        boolean result = communityLikesEntity.findByBoard_id(board_id); // 게시글 목록에서 해당 게시글이 있는지 확인

        if (result) { //게시글 목록에 해당 게시글 존재 시
            communityLikesEntity.removeLikedBoardId(board_id);
            currentLikeCount -= 1;
        } else {
            communityLikesEntity.addLikedBoardId(board_id);
            currentLikeCount += 1;
        }

        // 데이터 저장
        collegeEntity.setLikeCount(currentLikeCount); // 총 좋아요 갯수 업데이트
        collegeRepository.save(collegeEntity); // 저장
        usersLikesCommunityRepository.save(communityLikesEntity); // 해당
    }

//-------------------------------------------------------------------------------------------------------

    // [#8. 좋아요한 게시글 전체 조회]
    public Page<CommunityDTO> findMyLikePostingsByUserSeq(String user_seq, Pageable pageable){
        // #1. 회원의 좋아요 '게시글 목록' 조회
        Optional<CommunityLikesEntity> communityLikesEntity = usersLikesCommunityRepository.findByUserSeq(user_seq);
        if (!communityLikesEntity.isPresent()) {// 해당유저가 좋아요를 누른 게시글이 하나도 없다면,
            throw new DataNotFoundException("해당 유저는 좋아요한 게시글이 하나도 없습니다.");
        }

        // #2. 좋아요한 게시글이 적어도 1개 이상 있을 때
        List<Long> likedBoardIds = communityLikesEntity.get().getBoardId(); //해당 회원의 좋아요를 누른 '게시글 번호' 목록을 불러온다.

        // #3. EntityList 생성
        //likedBoardIds 리스트에 있는 모든 '게시글 번호' 에 해당하는 CommunityEntity 객체들을 가져온다.
        List<CollegeEntity> collegeEntities = collegeRepository.findAllById(likedBoardIds);

        // #4. 페이징 처리를 위한 시작 인덱스(start)와 종료 인덱스(end)를 계산하는 로직
        int start = (int) pageable.getOffset(); //현재 페이지에서 첫 번째 게시글의 '오프셋(시작 인덱스)'을 반환

        // #5. 현재 페이지에서 마지막 게시글의 인덱스를 계산
        int end = Math.min((start + pageable.getPageSize()), collegeEntities.size());

        // #6. 현재 페이지에 속하는 부분 리스트(subList(start, end))를 생성하고,
        // 각 엔티티 객체들을 DTO로 변환하여 새로운 리스트로 수집합니다(collect(Collectors.toList())).
        List<CommunityDTO> communityDTOS = collegeEntities.subList(start, end).stream()
                .map(CommunityDTO::toCommunityDTO)
                .collect(Collectors.toList());

        // #7. 생성된 DTO 리스트와 원래의 페이징 정보(pageable), 그리고 전체 엔티티 개수(collegeEntities.size()) 등을 함께 제공하여 새로운 Page 객체 (PageImpl) 를 생성하고 반환합니다.
        return new PageImpl<>(communityDTOS, pageable, collegeEntities.size());
    }

//-------------------------------------------------------------------------------------------------------

    // [#9. 키워드로 게시글 전체 조회]
    @Transactional
    public Page<CommunityDTO> findAllByKeyword(String keyword, Pageable pageable) {
        Page<CollegeEntity> collegeEntityList = collegeRepository.findByKeyword(keyword, pageable);// boardRepository 에서 findByKeyword 메서드를 호출하여 keyword가 포함된 모든 게시글 데이터를 boardEntityList 에 저장
        List<CommunityDTO> communityDTOList = collegeEntityList.stream() //List<BoardDTO>로 변환하기 위해 Java Stream 을 사용하여 각 BoardEntity 객체를 BoardDTO.toBoardDTO 메서드를 사용하여 변환한 후
                .map(CommunityDTO::toCommunityDTO)
                .collect(Collectors.toList()); // collect 메서드를 통해 다시 리스트 형태로 변환하여 boardDTOList 에 저장
        return new PageImpl<>(communityDTOList, pageable, collegeEntityList.getTotalElements());
    }

//--------------------------------------------------------------------------------------------------------

// [#11. 댓글 입력]
    public void CommentSave(CommentDTO commentDTO) {
        CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO); //DTO -> Entity 변환
        commentRepository.save(commentEntity);
}

//-------------------------------------------------------------------------------------------------------

    // [#12. 대댓글 입력]
    @Transactional
        /*
         위와 같은 코드는 한 번의 트랜잭션에서 처리되어야 합니다.
         그래야만 Hibernate 가 변경된 객체 상태를 데이터베이스와 동기화할 수 있기 때문입니다.
         따라서 위 메서드 전체를 Spring의 @Transactional 어노테이션으로 감싸주는 것을 권장합니다.
         */
    public void ChildCommentSave(Long comment_id, CommentDTO commentDTO) {

        // parentCommentId 값 설정
        if (comment_id != null) { // 부모댓글의 commentId 가 존재한다면,
            CommentEntity parentCommentEntity = commentRepository.findById(comment_id) //해당 commentId 값을 가진 부모 댓글 엔티티를 가져온다.
                    .orElseThrow(() -> new IllegalArgumentException("Invalid parent comment id: " + comment_id));

            CommentEntity childCommentEntity = CommentEntity.toSaveEntity(commentDTO); // 대댓글 Entity 생성.

            //해당 대댓글은 부모 댓글의 자식 리스트에 추가 (일대다 관계 설정)
            parentCommentEntity.getChildComments().add(childCommentEntity);
            //부모 댓글의 comment_id를 대댓글 CommentDTO 의 parentCommentId 필드에 설정합니다.
            childCommentEntity.setParent(parentCommentEntity);
            // DB 저장
            commentRepository.save(childCommentEntity);
        }

    }

//-------------------------------------------------------------------------------------------------------


    // [#13. 댓글 상세 조회]
    @Transactional
    public List<CommentDTO> getCommentsAndChildComments(Long board_id) {
        // 해당 게시글에 쓰인 모든 '부모댓글'을 리스트로 가져온다.
        List<CommentEntity> parentCommentEntities = commentRepository.findByBoardId(board_id);

        // parentCommentEntity 리스트를 'parentCommentDTO 리스트' 로 모두 변환
        List<CommentDTO> parentCommentDTOs = parentCommentEntities.stream()
                .map(CommentDTO::toCommentDTO)
                .collect(Collectors.toList());

        // 각 CommentDTO 에 대해 특정 부모댓글에 대한 ->  자식 댓글들도 조회하여 설정
        for (CommentDTO parentCommentDTO : parentCommentDTOs) {
            Long parentCommentId = parentCommentDTO.getComment_id();
            List<CommentEntity> childCommentEntities =
                    commentRepository.findByParentCommentEntity_Id(parentCommentId);

            // ChildCommentEntity 리스트를 'ChildCommentDTO리스트'로 변환
            List<CommentDTO> childCommentDTOS = childCommentEntities.stream()
                    .map(CommentDTO::toCommentDTO)
                    .collect(Collectors.toList());

            //부모 댓글 DTO에 자식 댓글 DTO들 설정
            parentCommentDTO.setChildComments(childCommentDTOS);
        }

        return parentCommentDTOs;
    }

}
