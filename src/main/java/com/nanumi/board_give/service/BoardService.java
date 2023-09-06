package com.nanumi.board_give.service;

import com.nanumi.board_give.DataNotFoundException.DataNotFoundException;
import com.nanumi.board_give.dto.BoardDTO;
import com.nanumi.board_give.entity.BoardEntity;
import com.nanumi.board_give.entity.UsersLikesEntity;
import com.nanumi.board_give.repository.BoardRepository;
import com.nanumi.board_give.repository.UsersLikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    // [Repository 객체 선언]
    private final BoardRepository boardRepository;
    private final UsersLikesRepository usersLikesRepository;

    // [#1. 나눔 게시글 등록 - BoardRepository 인터페이스 호출 -> controller 로 반환 할 필요 없다. DB에 등록만 하면되니까]
    public void give_posting_save(BoardDTO boardDTO) {

        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO); //DTO -> Entity 변환
        boardRepository.save(boardEntity);
    }


//------------------------------------------------------------------------------------------------------------------


    //[#2. (타인) 나눔 게시글 전체 조회]
    @Transactional
    public List<BoardDTO> give_posting_findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll(); //리포지토리에서 데이터들은 List 형태의 Entity 가 넘어오게 된다.

        // Entity -> Dto 로 옮겨 닮아서, controller 로 반환해줘야 한다.
        List<BoardDTO> boardDTOList = new ArrayList<>(); // 새로운 ArrayList 선언
        for (BoardEntity boardEntity : boardEntityList) { //foreach 반복문을 이용해서, 데이터들을 하나식 꺼내서 boardEntity 객체에 담는다.
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity)); //boardEntity -> boardDTO 로 바꿔서 boardDTOList 에 하나씩 주입
        }
        return boardDTOList; //컨트롤러로 반환.
    }

    //-------------------------------------------------------------------------------------------------------

    //[#3. (타인)나눔 게시글 상세 조회]
    @Transactional
    public Optional<BoardDTO> give_posting_findById(Long board_give_id) {
        Optional<BoardEntity> boardEntity = boardRepository.findById(board_give_id);
        return boardEntity.map(BoardDTO::toBoardDTO); //map() 메소드를 사용하여, Optional<BoardEntity> 내부의 BoardEntity 객체가 존재할 경우에만 BoardDTO.toBoardDTO() 메소드를 호출하여 Optional<BoardDTO> 객체로 변환하였습니다.
    }


    //-------------------------------------------------------------------------------------------------------

    //[#4. (개인) 나눔 게시글 전체 조회] - BoardRepository 사용
    @Transactional
    public List<BoardDTO> findAllPostingsByUserSeq(Long user_seq) {
        List<BoardEntity> boardEntityList = boardRepository.findAllByUserSeq(user_seq); // boardRepository 에서 findAllByUserId 메서드를 호출하여 userId에 해당하는 회원이 작성한 모든 게시글 데이터를 boardEntityList 에 저장
        List<BoardDTO> boardDTOList = boardEntityList.stream() //List<BoardDTO>로 변환하기 위해 Java Stream 을 사용하여 각 BoardEntity 객체를 BoardDTO.toBoardDTO 메서드를 사용하여 변환한 후
                .map(BoardDTO::toBoardDTO)
                .collect(Collectors.toList()); // collect 메서드를 통해 다시 리스트 형태로 변환하여 boardDTOList 에 저장
        return boardDTOList;
    }



    //-------------------------------------------------------------------------------------------------------

    //[#6. (개인) 나눔 게시글 수정]
    @Transactional
    public void updatePartOfBoard(BoardDTO boardDTO) { //수정된 게시글 데이터를 BoardDTO 객체로 받아온다.
        //요청된 게시물의 '회원 일련번호' 로 기존 게시물 '엔티티' 조회
        BoardEntity boardEntity = boardRepository.findByUserSeq(boardDTO.getUser_seq());

        boardEntity.setBoard_title(boardDTO.getBoard_title()); // 게시글 제목 업데이트
        boardEntity.setUser_seq(boardDTO.getUser_seq()); // 회원 일련번호 업데이트
        boardEntity.setCategory_id(boardDTO.getCategory_id()); // 책 카테고리 아이디 업데이트
        boardEntity.setBook_story(boardDTO.getBook_story()); // 책 내용 업데이트
        boardEntity.setState_underscore(boardDTO.getState_underscore()); // 상태_밑줄 흔적 업데이트
        boardEntity.setState_notes(boardDTO.getState_notes()); // 상태_필기 흔적 업데이트
        boardEntity.setState_cover(boardDTO.getState_cover()); // 상태_겉표지 상태 업데이트
        boardEntity.setState_written_name(boardDTO.getState_written_name()); // 상태_이름 기입 업데이트
        boardEntity.setState_page_color_change(boardDTO.getState_page_color_change()); // 상태_페이지 변색 업데이트
        boardEntity.setState_page_damage(boardDTO.getState_page_damage()); // 상태_페이지_손상 업데이트
        boardEntity.setCity_id(boardDTO.getCity_id()); // 지역 아이디 업데이트
        boardEntity.setMeet_want_location(boardDTO.getMeet_want_location()); // 거래 희망 지역 업데이트

        // 변경된 게시물 엔티티 저장
        boardRepository.save(boardEntity);
    }



    /*
    데이터베이스는 일반적으로 테이블 형태로 구성되며, 각 행(row)은 레코드(record)라고 부르는 저장 단위를 갖고 있습니다.
    엔티티 매니저가 데이터베이스 테이블에 새로운 레코드를 추가하는 경우, 새로운 row 가 추가되고 해당 row 에 대응하는 객체의 필드 값들이 저장됩니다.
    반면, 이미 존재하는 레코드의 특정 필드 값을 업데이트하는 경우, 해당 레코드의 해당 필드에 대한 값이 변경됩니다.
    따라서, 위 코드에서는 기존 게시물의 필드 값을 변경하는 것이며, 새로운 공간을 생성하는 것은 아닙니다.

     */

//-------------------------------------------------------------------------------------------------------

    // [#7. 좋아요 누르기]

    @Transactional
    public void addLikeToPost(Long user_seq, Long board_give_id) {

        // 1. 해당 게시글의 모든 데이터를 가져온다.
        Optional<BoardEntity> boardEntity = boardRepository.findById(board_give_id);
        if (boardEntity.isPresent()) { // 해당게시글의 데이터가 존재한다면, 그 중 '현재 좋아요 총 갯수 ' 를 불러온다.
            Long currentLikeCount = boardEntity.get().getLikeCount() != null ? boardEntity.get().getLikeCount() : 0L; //boardEntity 객체에 저장되어 있는 값을 가져오기 위해 get() 메소드를 호출 & getLikeCount() 메소드 이용해 좋아요 갯수 가져오기

            // 2. 사용자 고유 번호를 사용해 usersLikesRepository 에서 해당 사용자가 좋아요를 누른 모든 게시글 id를 불러옴
            Optional<UsersLikesEntity> existingLike = usersLikesRepository.findByUserSeq(user_seq);

            // 사용자가 좋아요를 누른 게시글이 하나도 없을떄
            if (!existingLike.isPresent()) {// !는 논리 부정 연산자(not operator)로, 해당 조건식이 true일 때 false를, false일 때 true를 반환합니다.
                UsersLikesEntity usersLikesEntity = new UsersLikesEntity(); //새로운 객체 선언
                usersLikesEntity.setUserSeq(user_seq); //특정 사용자 새롭게 지정
                usersLikesEntity.addLikedBoardId(board_give_id); // 게시글 List<> 에 , 해당 게시글이 처음으로 추가
                usersLikesRepository.save(usersLikesEntity); // 해당게시글에, 해당 사용자가 좋아요를 눌렀다는 정보를 갱신

                //해당 게시글의 좋아요 총 개수 업데이트
                boardEntity.get().setLikeCount(currentLikeCount + 1); //현재 좋아요 갯수 + 1

                boardRepository.save(boardEntity.get());
            }
            // 사용자가 좋아요를 누른 게시글이 하나라도 있을때
             else if (existingLike.isPresent()) {
                 boolean result = existingLike.get().findByBoard_give_id(board_give_id); // 게시글 목록에서 해당 게시글이 있는지 확인
                if (result == false) { //게시글 목록에 해당 게시글이 없다면
                    existingLike.get().addLikedBoardId(board_give_id); //게시글 번호 푸가
                    usersLikesRepository.save(existingLike.get());
                    boardEntity.get().setLikeCount(currentLikeCount + 1);
                    boardRepository.save(boardEntity.get());
                } else { //게시글 목록에 해당 게시글 존재시
                    existingLike.get().removeLikedBoardId(board_give_id);
                    usersLikesRepository.save(existingLike.get());
                    boardEntity.get().setLikeCount(currentLikeCount - 1);
                    boardRepository.save(boardEntity.get());
                }
             }
        } else {
            throw new DataNotFoundException("게시글이 존재하지 않습니다.");
        }
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
    public List<BoardDTO> findMyLikePostingsByUserSeq(Long user_seq) {
        Optional<UsersLikesEntity> usersLikesEntity = usersLikesRepository.findByUserSeq(user_seq); //회원의 좋아요 게시글 목록 조회
        if (!usersLikesEntity.isPresent()) {
            throw new DataNotFoundException("해당 유저는 좋아요한 게시글이 하나도 없슶니다.");
        }

        List<Long> likedBoardIds = usersLikesEntity.get().getBoardGiveId(); //해당 회원의 게시글 목록을 불러온다.
        List<BoardEntity> boardEntityList = new ArrayList<>();

        for (Long board_give_id : likedBoardIds) { // 게시글 목록에서, 게시글 번호를 하나씩 꺼낸다.
            // boardRepository에서 모든 게시글번호를 가진 boardEntity를 boardEntityList에 담는다.
            Optional<BoardEntity> boardEntity = boardRepository.findById(board_give_id);
            if (boardEntity.isPresent()) { //해당 게시글의 정보가 존재하면
                boardEntityList.add(boardEntity.get()); //리스트에 Entity 객체 1개씩 넣기
            }
        }

        List<BoardDTO> boardDTOList = boardEntityList.stream() //List<BoardDTO>로 변환하기 위해 Java Stream 을 사용하여 각 BoardEntity 객체를 BoardDTO.toBoardDTO 메서드를 사용하여 변환한 후
                .map(BoardDTO::toBoardDTO)
                .collect(Collectors.toList()); // collect 메서드를 통해 다시 리스트 형태로 변환하여 boardDTOList 에 저장
        return boardDTOList;
    }

//-------------------------------------------------------------------------------------------------------

    // [#9. 키워드로 게시글 전체 조회]
    @Transactional
    public List<BoardDTO> findAllByKeyword(String keyword) {
        List<BoardEntity> boardEntityList = boardRepository.findByKeyword(keyword); // boardRepository 에서 findByKeyword 메서드를 호출하여 keyword가 포함된 모든 게시글 데이터를 boardEntityList 에 저장
        List<BoardDTO> boardDTOList = boardEntityList.stream() //List<BoardDTO>로 변환하기 위해 Java Stream 을 사용하여 각 BoardEntity 객체를 BoardDTO.toBoardDTO 메서드를 사용하여 변환한 후
                .map(BoardDTO::toBoardDTO)
                .collect(Collectors.toList()); // collect 메서드를 통해 다시 리스트 형태로 변환하여 boardDTOList 에 저장
        return boardDTOList;
    }
}






