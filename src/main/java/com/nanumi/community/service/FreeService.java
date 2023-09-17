package com.nanumi.community.service;

import com.nanumi.board_give.DataNotFoundException.DataNotFoundException;
import com.nanumi.community.dto.CommunityDTO;
import com.nanumi.community.entity.FreeEntity;
import com.nanumi.community.entity.UsersLikesEntity.CommunityLikesEntity;
import com.nanumi.community.repository.FreeRepository;
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
public class FreeService  {
    private final FreeRepository freeRepository;
    private final UsersLikesCommunityRepository usersLikesCommunityRepository;

    //[#1. 자유 게시글 등록 ]
    public void PostingSave(CommunityDTO communityDTO) {

        FreeEntity freeEntity = FreeEntity.toSaveFreeEntity(communityDTO); //DTO -> Entity 변환
        freeRepository.save(freeEntity);
    }



    //------------------------------------------------------------------------------------------------------------------
    // [#2. 게시글 전체 조회]
    @Transactional
    public Page<CommunityDTO> postingFindAll(Pageable pageable) {
        Page<FreeEntity> freeEntityList = freeRepository.findAll(pageable); //리포지토리에서 데이터들은 List 형태의 Entity 가 넘어오게 된다.

        // Entity -> Dto 로 옮겨 닮아서, controller 로 반환해줘야 한다.
        return new PageImpl<>(freeEntityList.stream().map(CommunityDTO::toCommunityDTO).collect(Collectors.toList()), pageable, freeEntityList.getTotalElements());
    }
    //-------------------------------------------------------------------------------------------------------

    //[#3. 게시글 상세 조회]
@Transactional
public Optional<CommunityDTO> postingFindById(Long board_id) {
     Optional<FreeEntity> freeEntity = freeRepository.findById(board_id);
     return freeEntity.map(CommunityDTO::toCommunityDTO); //map() 메소드를 사용하여, Optional<BoardEntity> 내부의 BoardEntity 객체가 존재할 경우에만 BoardDTO.toBoardDTO() 메소드를 호출하여 Optional<BoardDTO> 객체로 변환하였습니다.
        //주어진 코드는 freeEntity 컬렉션 또는 스트림의 각 요소를 CommunityDTO 객체로 매핑하여 새로운 컬렉션 또는 스트림을 생성하는 것을 나타냅니다. 결과적으로 freeEntity의 각 요소가 CommunityDTO 객체로 변환된 새로운 컬렉션 또는 스트림이 반환될 것입니다.
    }

    //-------------------------------------------------------------------------------------------------------

    //[#4. (개인) 게시글 전체 조회] - BoardRepository 사용
    @Transactional
    public Page<CommunityDTO> findAllPostingsByUserSeq(String user_seq, Pageable pageable) {
        Page<FreeEntity> freeEntityPage = freeRepository.findAllByUserSeq(user_seq, pageable); // Pageable 객체 추가
        return freeEntityPage.map(CommunityDTO::toCommunityDTO); // 각 BoardEntity 객체를 BoardDTO로 변환한 후 페이지 형태로 반환
    }



    //-------------------------------------------------------------------------------------------------------

    //[#6. (개인) 게시글 수정]
    @Transactional
    public void updatePartOfBoard(Long board_id, CommunityDTO communityDTO) {
        //요청된 게시물의 '게시글 번호' 로 -----> 기존 게시물 '엔티티' 조회
        Optional<FreeEntity> optionalFreeEntity = freeRepository.findById(board_id);

        if(optionalFreeEntity.isPresent()) { // 게시글 엔티티가 존재시, 해당 데이터들을 모두 불러온다.
            FreeEntity freeEntity = optionalFreeEntity.get();

            //#1. CommunityDTO 로부터 받은 값이 있는 경우 : 그 값(communityDTO.getBoard_title())을 freeEntity 에 설정
                     //만약, CommunityDTO 로부터 값을 못 받은 경우 : 기존 값(freeEntity.getBoard_title()))을 freeEntity 에 설정
            freeEntity.setBoard_title(Optional.ofNullable(communityDTO.getBoard_title()).orElse(freeEntity.getBoard_title())); // 게시글 제목
            freeEntity.setUser_seq(Optional.ofNullable(communityDTO.getUser_seq()).orElse(freeEntity.getUser_seq())); // 회원 일련번호
            freeEntity.setCategory_id(Optional.ofNullable(communityDTO.getCategory_id()).orElse(freeEntity.getCategory_id())); //카테고리 아이디
            freeEntity.setBoard_story(Optional.ofNullable(communityDTO.getBoard_story()).orElse(freeEntity.getBoard_story())); // 게시글 내용
            freeEntity.setUserGrade(Optional.ofNullable(communityDTO.getUserGrade()).orElse(freeEntity.getUserGrade())); // 사용자 등급
            freeEntity.setNickName(Optional.ofNullable(communityDTO.getNickName()).orElse(freeEntity.getNickName())); // 사용자 닉네임
            freeEntity.setDate(Optional.ofNullable(communityDTO.getDate()).orElse(freeEntity.getDate())); // 게시글 작성 시간(날짜)
            freeEntity.setUserImageUrl(Optional.ofNullable(communityDTO.getUserImageUrl()).orElse(freeEntity.getUserImageUrl())); //사용자 프로필 이미지
            freeEntity.setLikeCount(Optional.ofNullable(communityDTO.getLikeCount()).orElse(freeEntity.getLikeCount())); // 좋아요 갯수
            freeEntity.setId(freeEntity.getId()); // 게시글 번호 (그대로 유지)
            // 추가 이미지들이 여러장 존재 시
            if (communityDTO.getBoardImageUrls() != null) {
                List<String> updatedImages = new ArrayList<>(freeEntity.getBoardImages()); // 기존 이미지를 가지는 새로운 이미지 객체 생성
                updatedImages.addAll(communityDTO.getBoardImageUrls());
                freeEntity.setBoardImages(updatedImages);
            } // 추가 이미지들이 없을 때
            freeEntity.setBoardImages(freeEntity.getBoardImages()); // 기존 이미지만 다시 세팅

            //#2.  변경된 게시물 엔티티 저장
            freeRepository.save(freeEntity);

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

// -------------------------------------------------------------------------------------------------------

    // [#7. 좋아요 누르기]

    @Transactional
    public void addLikeToPost(String user_seq, Long board_id) {

        // 1. 해당 게시글의 모든 데이터를 가져온다.
        FreeEntity freeEntity = freeRepository.findById(board_id) // findById 메소드의 반환타입은 항상 Optional<T>
                .orElseThrow(() -> new DataNotFoundException("게시글이 존재하지 않습니다."));

        // 2. 현재 해당 게시글의 총 좋아요 갯수를 가져온다.
        Long currentLikeCount = Optional.ofNullable(freeEntity.getLikeCount()).orElse(0L);
        /*
        Optional.ofNullable() 메서드는 null이 아닌 값을 가진 경우에 해당 값을 감싸는 Optional 객체를 생성합니다.
        만약 likeCount가 null이라면, 대신에 0L로 초기화된 Optional 객체가 생성됩니다.
        따라서 위의 코드는 boardEntity.getLikeCount()가 null인 경우에도 NullPointerException을 방지하기 위해
        0L로 초기화된 형태의 long 변수인 currentLikeCount를 얻기 위한 방법입니다.
         */

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
        freeEntity.setLikeCount(currentLikeCount); // 총 좋아요 갯수 업데이트
        freeRepository.save(freeEntity); // 저장
        usersLikesCommunityRepository.save(communityLikesEntity); // 해당
    }

    /*
1. 사용자의 usersLikesEntity 객체가 존재하는지 여부에 따라 달라진다.
            2. 존재 X
            새롭게 객체 생성
            특정 사용자 변수 값 설정
            게시글 List에 해당 게시글 번호 처음으로 추가
            해당 내용을 리포지토리에 저장(usersLikesRepository)

            전체 좋아요 1추가
            해당 내용을 리포토리에 저장(boardEntity)

            3. 존재 O
            기존객체 불러오기
            게시글 List에 해당 게시글 번호 조회

                게시글 번호 없다면
                    게시글 번호를 게시글 List에 추가
                    해당 내용을 리포지토리에 저장(usersLikesRepository)
                    전체 좋아요 1추가

                게시글 번호가 있다면
                    게시글 번호를 게시글 List에서 삭제
                    해당 내용을 리포지토리에 저장(usersLikesRepository)
                    전체 좋아요 1감소

            해당 내용을 리포토리에 저장(boardEntity)
 */
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
        List<FreeEntity> freeEntities = freeRepository.findAllById(likedBoardIds);

        // #4. 페이징 처리를 위한 시작 인덱스(start)와 종료 인덱스(end)를 계산하는 로직
        int start = (int) pageable.getOffset(); //현재 페이지에서 첫 번째 게시글의 '오프셋(시작 인덱스)'을 반환
        /*오프셋은 데이터 목록에서 특정 항목이 '시작되는 위치'를 나타내며, 0부터 시작합니다.
          ex)  6개의 게시글들을 한 페이지에 보여주는 경우 : 첫 번째 페이지의 오프셋은 0이고, 페이지 인덱스는 총 0,1,2,3,4,5 가 있다.
                                                    두 번째 페이지의 오프셋은 6이고, 페이지 인덱스는 총 6,7,8,9,10,11 이 있다. */

        // #5. 현재 페이지에서 마지막 게시글의 인덱스를 계산
        int end = Math.min((start + pageable.getPageSize()), freeEntities.size());
        //(start + pageable.getPageSize())는 현재 페이지가 가득 찼을 때 마지막 게시글 다음 위치인덱스(즉, 다음 페이지의 첫번째 게시글 위치인덱스)를 나타낸다.
        // 하지만 실제 게시글 수(boardEntities.size())가 이보다 작을 수 있으므로 Math.min() 함수를 사용하여 두 값 중 작은 값을 선택합니다.
        // (start + pageable.getPageSize())가 반환하는 값은 실제로 마지막 항목 다음 위치를 가리키므로, 실제 마지막 항목의 인덱스보다 1이 큽니다.
        // "현재 페이지 내 마지막 요소 위치"에 해당하는 값을 얻으려면 이 결과값에서 -1을 해야합니다. = end -1


        // #6. 현재 페이지에 속하는 부분 리스트(subList(start, end))를 생성하고,
        // 각 엔티티 객체들을 DTO로 변환하여 새로운 리스트로 수집합니다(collect(Collectors.toList())).
        List<CommunityDTO> communityDTOS = freeEntities.subList(start, end).stream()
                .map(CommunityDTO::toCommunityDTO)
                .collect(Collectors.toList());

        // #7. 생성된 DTO 리스트와 원래의 페이징 정보(pageable), 그리고 전체 엔티티 개수(freeEntities.size()) 등을 함께 제공하여 새로운 Page 객체 (PageImpl) 를 생성하고 반환합니다.
        return new PageImpl<>(communityDTOS, pageable, freeEntities.size());
    }

//-------------------------------------------------------------------------------------------------------

    // [#9. 키워드로 게시글 전체 조회]
    @Transactional
    public Page<CommunityDTO> findAllByKeyword(String keyword, Pageable pageable) {
        Page<FreeEntity> freeEntityList = freeRepository.findByKeyword(keyword, pageable);// boardRepository 에서 findByKeyword 메서드를 호출하여 keyword가 포함된 모든 게시글 데이터를 boardEntityList 에 저장
        List<CommunityDTO> communityDTOList = freeEntityList.stream() //List<BoardDTO>로 변환하기 위해 Java Stream 을 사용하여 각 BoardEntity 객체를 BoardDTO.toBoardDTO 메서드를 사용하여 변환한 후
                .map(CommunityDTO::toCommunityDTO)
                .collect(Collectors.toList()); // collect 메서드를 통해 다시 리스트 형태로 변환하여 boardDTOList 에 저장
        return new PageImpl<>(communityDTOList, pageable, freeEntityList.getTotalElements());
    }
}
