package com.nanumi.community.service;

import com.nanumi.community.dto.CommunityDTO;
import com.nanumi.community.entity.CollegeEntity;
import com.nanumi.community.entity.CommunityEntity;
import com.nanumi.community.entity.FreeEntity;
import com.nanumi.community.entity.LearnEntity;
import com.nanumi.community.repository.LearnRepository;
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
public class LearnService {
    private final LearnRepository learnRepository;

    //[#1. 학습 게시글 등록 ]
    public void PostingSave(CommunityDTO communityDTO) {
        LearnEntity learnEntity = LearnEntity.toSaveLearnEntity(communityDTO); //DTO -> Entity 변환
        learnRepository.save(learnEntity);
    }



//------------------------------------------------------------------------------------------------------------------
    //[#2. 게시글 전체 조회]
    @Transactional
    public Page<CommunityDTO> postingFindAll(Pageable pageable) {
        Page<LearnEntity> learnEntityList = learnRepository.findAll(pageable); //리포지토리에서 데이터들은 List 형태의 Entity 가 넘어오게 된다.

        // Entity -> Dto 로 옮겨 닮아서, controller 로 반환해줘야 한다.
        return new PageImpl<>(learnEntityList.stream().map(CommunityDTO::toCommunityDTO).collect(Collectors.toList()), pageable, learnEntityList.getTotalElements());
    }
    //-------------------------------------------------------------------------------------------------------

    //[#3. 게시글 상세 조회]
    @Transactional
    public Optional<CommunityDTO> postingFindById(Long board_id) {
        Optional<LearnEntity> learnEntity = learnRepository.findById(board_id);
        return learnEntity.map(CommunityDTO::toCommunityDTO); //map() 메소드를 사용하여, Optional<BoardEntity> 내부의 BoardEntity 객체가 존재할 경우에만 BoardDTO.toBoardDTO() 메소드를 호출하여 Optional<BoardDTO> 객체로 변환하였습니다.
    }

    //-------------------------------------------------------------------------------------------------------

    //[#4. (개인) 게시글 전체 조회] - BoardRepository 사용
    @Transactional
    public Page<CommunityDTO> findAllPostingsByUserSeq(String user_seq, Pageable pageable) {
        Page<LearnEntity> learnEntityPage = learnRepository.findAllByUserSeq(user_seq, pageable); // Pageable 객체 추가
        return learnEntityPage.map(CommunityDTO::toCommunityDTO); // 각 BoardEntity 객체를 BoardDTO로 변환한 후 페이지 형태로 반환
    }

    //-------------------------------------------------------------------------------------------------------

    //[#6. (개인) 게시글 수정]
    @Transactional
    public void updatePartOfBoard(Long board_id, CommunityDTO communityDTO) {
        //요청된 게시물의 '게시글 번호' 로 -----> 기존 게시물 '엔티티' 조회
        Optional<LearnEntity> optionalLearnEntity = learnRepository.findById(board_id);

        if(optionalLearnEntity.isPresent()) { // 게시글 엔티티가 존재시, 해당 데이터들을 모두 불러온다.
            LearnEntity learnEntity = optionalLearnEntity.get();

            //#1. CommunityDTO 로부터 받은 값이 있는 경우 : 그 값(communityDTO.getBoard_title())을 freeEntity 에 설정
            //만약, CommunityDTO 로부터 값을 못 받은 경우 : 기존 값(freeEntity.getBoard_title()))을 freeEntity 에 설정
            learnEntity.setBoard_title(Optional.ofNullable(communityDTO.getBoard_title()).orElse(learnEntity.getBoard_title())); // 게시글 제목
            learnEntity.setUser_seq(Optional.ofNullable(communityDTO.getUser_seq()).orElse(learnEntity.getUser_seq())); // 회원 일련번호
            learnEntity.setCategory_id(Optional.ofNullable(communityDTO.getCategory_id()).orElse(learnEntity.getCategory_id())); //카테고리 아이디
            learnEntity.setBoard_story(Optional.ofNullable(communityDTO.getBoard_story()).orElse(learnEntity.getBoard_story())); // 게시글 내용
            learnEntity.setUserGrade(Optional.ofNullable(communityDTO.getUserGrade()).orElse(learnEntity.getUserGrade())); // 사용자 등급
            learnEntity.setNickName(Optional.ofNullable(communityDTO.getNickName()).orElse(learnEntity.getNickName())); // 사용자 닉네임
            learnEntity.setDate(Optional.ofNullable(communityDTO.getDate()).orElse(learnEntity.getDate())); // 게시글 작성 시간(날짜)
            learnEntity.setUserImageUrl(Optional.ofNullable(communityDTO.getUserImageUrl()).orElse(learnEntity.getUserImageUrl())); //사용자 프로필 이미지
            learnEntity.setLikeCount(Optional.ofNullable(communityDTO.getLikeCount()).orElse(learnEntity.getLikeCount())); // 좋아요 갯수
            learnEntity.setId(learnEntity.getId()); // 게시글 번호 (그대로 유지)
            // 추가 이미지들이 여러장 존재 시
            if (communityDTO.getBoardImageUrls() != null) {
                List<String> updatedImages = new ArrayList<>(learnEntity.getBoardImages()); // 기존 이미지를 가지는 새로운 이미지 객체 생성
                updatedImages.addAll(communityDTO.getBoardImageUrls());
                learnEntity.setBoardImages(updatedImages);
            } // 추가 이미지들이 없을 때
            learnEntity.setBoardImages(learnEntity.getBoardImages()); // 기존 이미지만 다시 세팅

            //#2.  변경된 게시물 엔티티 저장
            learnRepository.save(learnEntity);

        } else { //해당 ID의 엔티티(기존 게시글)가 존재하지 않는다면 NoSuchElementException 을 발생시켜, 이 메소드를 호출한 곳에서 이 예외를 처리할 수 있도록 합니다.
            throw new NoSuchElementException("No Board found with id: " + board_id);
        }
    }



    /*
    데이터베이스는 일반적으로 테이블 형태로 구성되며, 각 행(row)은 레코드(record)라고 부르는 저장 단위를 갖고 있습니다.
    엔티티 매니저가 데이터베이스 테이블에 새로운 레코드를 추가하는 경우, 새로운 row 가 추가되고 해당 row 에 대응하는 객체의 필드 값들이 저장됩니다.
    반면, 이미 존재하는 레코드의 특정 필드 값을 업데이트하는 경우, 해당 레코드의 해당 필드에 대한 값이 변경됩니다.
    따라서, 위 코드에서는 기존 게시물의 필드 값을 변경하는 것이며, 새로운 공간을 생성하는 것은 아닙니다.

     */

////-------------------------------------------------------------------------------------------------------
//
//    // [#7. 좋아요 누르기]
//
//    @Transactional
//    public void addLikeToPost(Long user_seq, Long board_give_id) {
//
//        // 1. 해당 게시글의 모든 데이터를 가져온다.
//        Optional<BoardEntity> boardEntity = boardRepository.findById(board_give_id);
//        if (boardEntity.isPresent()) { // 해당게시글의 데이터가 존재한다면, 그 중 '현재 좋아요 총 갯수 ' 를 불러온다.
//            Long currentLikeCount = boardEntity.get().getLikeCount() != null ? boardEntity.get().getLikeCount() : 0L; //boardEntity 객체에 저장되어 있는 값을 가져오기 위해 get() 메소드를 호출 & getLikeCount() 메소드 이용해 좋아요 갯수 가져오기
//
//            // 2. 사용자 고유 번호를 사용해 usersLikesRepository 에서 해당 사용자가 좋아요를 누른 모든 게시글 id를 불러옴
//            Optional<UsersLikesEntity> existingLike = usersLikesRepository.findByUserSeq(user_seq);
//
//            // 사용자가 좋아요를 누른 게시글이 하나도 없을떄
//            if (!existingLike.isPresent()) {// !는 논리 부정 연산자(not operator)로, 해당 조건식이 true일 때 false를, false일 때 true를 반환합니다.
//                UsersLikesEntity usersLikesEntity = new UsersLikesEntity(); //새로운 객체 선언
//                usersLikesEntity.setUserSeq(user_seq); //특정 사용자 새롭게 지정
//                usersLikesEntity.addLikedBoardId(board_give_id); // 게시글 List<> 에 , 해당 게시글이 처음으로 추가
//                usersLikesRepository.save(usersLikesEntity); // 해당게시글에, 해당 사용자가 좋아요를 눌렀다는 정보를 갱신
//
//                //해당 게시글의 좋아요 총 개수 업데이트
//                boardEntity.get().setLikeCount(currentLikeCount + 1); //현재 좋아요 갯수 + 1
//
//                boardRepository.save(boardEntity.get());
//            }
//            // 사용자가 좋아요를 누른 게시글이 하나라도 있을때
//            else if (existingLike.isPresent()) {
//                boolean result = existingLike.get().findByBoard_give_id(board_give_id); // 게시글 목록에서 해당 게시글이 있는지 확인
//                if (result == false) { //게시글 목록에 해당 게시글이 없다면
//                    existingLike.get().addLikedBoardId(board_give_id); //게시글 번호 푸가
//                    usersLikesRepository.save(existingLike.get());
//                    boardEntity.get().setLikeCount(currentLikeCount + 1);
//                    boardRepository.save(boardEntity.get());
//                } else { //게시글 목록에 해당 게시글 존재시
//                    existingLike.get().removeLikedBoardId(board_give_id);
//                    usersLikesRepository.save(existingLike.get());
//                    boardEntity.get().setLikeCount(currentLikeCount - 1);
//                    boardRepository.save(boardEntity.get());
//                }
//            }
//        } else {
//            throw new DataNotFoundException("게시글이 존재하지 않습니다.");
//        }
//    }
//
//    /*
//1. 사용자의 usersLikesEntity 객체가 존재하는지 여부에 따라 달라진다.
//            2. 존재 X
//            새롭게 객체 생성
//            특정 사용자 변수 값 설정
//            게시글 List에 해당 게시글 번호 처음으로 추가
//            해당 내용을 리포지토리에 저장(usersLikesRepository)
//
//            전체 좋아요 1추가
//            해당 내용을 리포토리에 저장(boardEntity)
//
//            3. 존재 O
//            기존객체 불러오기
//            게시글 List에 해당 게시글 번호 조회
//
//                게시글 번호 없다면
//                    게시글 번호를 게시글 List에 추가
//                    해당 내용을 리포지토리에 저장(usersLikesRepository)
//                    전체 좋아요 1추가
//
//                게시글 번호가 있다면
//                    게시글 번호를 게시글 List에서 삭제
//                    해당 내용을 리포지토리에 저장(usersLikesRepository)
//                    전체 좋아요 1감소
//
//            해당 내용을 리포토리에 저장(boardEntity)
// */
////-------------------------------------------------------------------------------------------------------
//
//    // [#8. 좋아요한 게시글 전체 조회]
//    public Page<BoardDTO> findMyLikePostingsByUserSeq(Long user_seq, Pageable pageable){
//        Optional<UsersLikesEntity> usersLikesEntity = usersLikesRepository.findByUserSeq(user_seq); //회원의 좋아요 게시글 목록 조회
//        if (!usersLikesEntity.isPresent()) {
//            throw new DataNotFoundException("해당 유저는 좋아요한 게시글이 하나도 없습니다.");
//        }
//        //#1. 좋아요한 게시글이 적어도 1개 이상 있을 때
//        List<Long> likedBoardIds = usersLikesEntity.get().getBoardGiveId(); //해당 회원의 '게시글 번호' 목록을 불러온다.
//
//        //#2. boardEntityList 생성
//        //likedBoardIds 리스트에 있는 모든 ID에 해당하는 BoardEntity 객체들을 반환
//        List<BoardEntity> boardEntities = boardRepository.findAllById(likedBoardIds);
//
//        //#3. 페이징 처리를 위한 시작 인덱스(start)와 종료 인덱스(end)를 계산하는 로직
//        int start = (int) pageable.getOffset(); //현재 페이지에서 첫 번째 게시글의 '오프셋(시작 인덱스)'을 반환
//        /*오프셋은 데이터 목록에서 특정 항목이 '시작되는 위치'를 나타내며, 0부터 시작합니다.
//          ex)  6개의 게시글들을 한 페이지에 보여주는 경우 : 첫 번째 페이지의 오프셋은 0이고, 페이지 인덱스는 총 0,1,2,3,4,5 가 있다.
//                                                    두 번째 페이지의 오프셋은 6이고, 페이지 인덱스는 총 6,7,8,9,10,11 이 있다. */
//
//        //현재 페이지에서 마지막 게시글의 인덱스를 계산
//        int end = Math.min((start + pageable.getPageSize()), boardEntities.size());
//        //(start + pageable.getPageSize())는 현재 페이지가 가득 찼을 때 마지막 게시글 다음 위치인덱스(즉, 다음 페이지의 첫번째 게시글 위치인덱스)를 나타낸다.
//        // 하지만 실제 게시글 수(boardEntities.size())가 이보다 작을 수 있으므로 Math.min() 함수를 사용하여 두 값 중 작은 값을 선택합니다.
//        // (start + pageable.getPageSize())가 반환하는 값은 실제로 마지막 항목 다음 위치를 가리키므로, 실제 마지막 항목의 인덱스보다 1이 큽니다.
//        // "현재 페이지 내 마지막 요소 위치"에 해당하는 값을 얻으려면 이 결과값에서 -1을 해야합니다. = end -1
//
//
//        //#4. 현재 페이지에 속하는 부분 리스트(subList(start, end))를 생성하고,
//        // 각 엔티티 객체들을 DTO로 변환하여 새로운 리스트로 수집합니다(collect(Collectors.toList())).
//        List<BoardDTO> boardDTOS = boardEntities.subList(start, end).stream()
//                .map(BoardDTO::toBoardDTO)
//                .collect(Collectors.toList());
//
//        //5. 생성된 DTO 리스트와 원래의 페이징 정보(pageable), 그리고 전체 엔티티 개수(boardEntities.size()) 등을 함께 제공하여 새로운 Page 객체 (PageImpl) 를 생성하고 반환합니다.
//        return new PageImpl<>(boardDTOS, pageable, boardEntities.size());
//    }
//
////-------------------------------------------------------------------------------------------------------
//
//    // [#9. 키워드로 게시글 전체 조회]
//    @Transactional
//    public Page<BoardDTO> findAllByKeyword(String keyword, Pageable pageable) {
//        Page<BoardEntity> boardEntityList = boardRepository.findByKeyword(keyword, pageable);// boardRepository 에서 findByKeyword 메서드를 호출하여 keyword가 포함된 모든 게시글 데이터를 boardEntityList 에 저장
//        List<BoardDTO> boardDTOList = boardEntityList.stream() //List<BoardDTO>로 변환하기 위해 Java Stream 을 사용하여 각 BoardEntity 객체를 BoardDTO.toBoardDTO 메서드를 사용하여 변환한 후
//                .map(BoardDTO::toBoardDTO)
//                .collect(Collectors.toList()); // collect 메서드를 통해 다시 리스트 형태로 변환하여 boardDTOList 에 저장
//        return new PageImpl<>(boardDTOList, pageable, boardEntityList.getTotalElements());
//    }
}
