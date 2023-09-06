package com.nanumi.board_give.controller;


import com.nanumi.board_give.dto.BoardDTO;
import com.nanumi.board_give.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController //이렇게 하면 모든 메서드는 기본적으로 @ResponseBody 가 있는 것처럼 동작해 반환 값이 JSON 으로 변환됩니다.
@RequiredArgsConstructor
public class BoardController {

    //[BoardService 객체 선언]
    private final BoardService boardService;

    //[#1. (개인) 나눔 게시글 등록 - BoardService 클래스 호출]
    @ResponseBody
    @PostMapping(value = "/posting/sharing", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> give_posting_save(@RequestBody BoardDTO boardDTO) {
        boardService.give_posting_save(boardDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "저장 완료");
        return new ResponseEntity<>(response, HttpStatus.OK); //200 응답코드 반환
    }


//------------------------------------------------------------------------------------------------------------------

    //[#2. (타인) 나눔 게시글 전체 조회]

    @GetMapping(value = "/posting/sharingAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> give_posting_findAll() {
        List<BoardDTO> boardDTOList = boardService.give_posting_findAll(); //서비스 객체에서 조회한 데이터 여러개를 DTO 객체에 담아서 List 자료구조에 주입
        Map<String, Object> response = new HashMap<>();
        response.put("boardTotalList", boardDTOList); //이 코드는 맵 객체에 가져온 데이터(boardDTOList)를 저장합니다. 이 때 데이터와 관련된 키("boardList")도 함께 저장합니다.
        // 이 키는 클라이언트가 JSON 데이터를 처리할 때 사용합니다.
        return ResponseEntity.ok(response); //이 코드는 맵 객체를 반환합니다. 이 때 ResponseEntity.ok() 를 사용하여 HTTP 응답 코드 200(성공)과 함께 맵 객체를 반환합니다.
    }                                       // 클라이언트는 이 데이터를 JSON 형식으로 받아 사용할 수 있습니다.


//------------------------------------------------------------------------------------------------------------------

    //[#3. (타인,개인) 나눔 게시글 상세 조회]
    @GetMapping(value = {"/posting/sharing", "/profile/sharing"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> give_posting_findById(@RequestParam("board_give_id") Long board_give_id)  {
        Optional<BoardDTO> optionalBoardDTO = boardService.give_posting_findById(board_give_id);
        if (optionalBoardDTO.isPresent()) { //Optional<BoardDTO> 객체 내부에 BoardDTO 객체가 존재하는 경우
            BoardDTO boardDTO = optionalBoardDTO.get();
            Map<String, Object> response = new HashMap<>();
            response.put("boardDTO", boardDTO); //response 에 Key 값 으로 boardDTO 를 저장하고, boardDTO 객체를 value 값으로 주입
            return ResponseEntity.ok(response); //200응답 코드와 같이 출력
        } else { //Optional<BoardDTO> 객체 내부에 BoardDTO 객체가 존재하지 않는 경우
            return ResponseEntity.notFound().build(); //404 응답 코드 반환
        }
    }

//------------------------------------------------------------------------------------------------------------------

    //[#4. (개인) 나눔 게시글 전체 조회] -> 해당 '회원 일련번호'가 들어간 모든 게시글의 데이터들을 찾아서 클라이언트에게 반환 -성공-
    @GetMapping(value = "/profile/sharingAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> findAllPostingsByUserSeq(@RequestParam("user_seq") Long user_seq) {
        List<BoardDTO> boardDTOList = boardService.findAllPostingsByUserSeq(user_seq); // boardService 에서 findAllPostingsByUserId 메서드를 호출해 모든 게시글 데이터를 가져온 후 이를 boardDTOList 에 저장
        Map<String, Object> response = new HashMap<>();
        response.put("boardDTOList", boardDTOList); //response 맵에 'boardDTOList' 라는 키와 함께 값을 저장
        return ResponseEntity.ok(response);
    }



//------------------------------------------------------------------------------------------------------------------
    /*   [#5. (개인) 나눔 게시글 상세 조회]

    => #3 과 동일한 메소드를 호출한다.
    두 개의 경로를 처리하고자 하는 경우, @GetMapping 애너테이션에서 경로를 '배열'로 설정하여 둘 이상의 경로를 지정할 수 있습니다.
    두 경로 중 하나로 호출이 들어올 때 동일한 메소드를 호출할 수 있습니다. 이렇게 하면 중복된 코드를 작성할 필요 없이 두 가지 경로를 모두 처리할 수 있습니다.  */

//------------------------------------------------------------------------------------------------------------------

    //[#6. (개인) 나눔 게시글 수정] -성공-        '수정' 버튼을 클릭과 동시에, 회원 일련번호를 서버로 보내야 한다.
    @ResponseBody
    @PostMapping("/profile/sharing/update")
    public ResponseEntity<Object> updatePartOfBoard(@RequestBody BoardDTO boardDTO) {
        boardService.updatePartOfBoard(boardDTO);  // 사용자가 입력한 정보를 토대로 기존 게시물의 일부 데이터를 업데이트합니다.
        Map<String, Object> response = new HashMap<>();
        response.put("message", "수정 완료");
        return new ResponseEntity<>(response, HttpStatus.OK); //200 응답코드 반환
    }
//-----------------------------------------------------------------------------------------------------


    //[#7. 좋아요/좋아요 취소 누르기]

    @ResponseBody
    @PostMapping("/LikeOrNot") // '좋아요' & '좋아요 취소' 기능을 토글하기 위한 API
    public ResponseEntity<Map<String, Object>> addLikeToPost(@RequestParam("user_seq") Long user_seq, @RequestParam("board_give_id") Long board_give_id) {

        boardService.addLikeToPost(user_seq, board_give_id);
        Map<String, Object> response = new HashMap<>();
        return ResponseEntity.ok(response);
    }


//-----------------------------------------------------------------------------------------------------

    //[#8. 좋아요한 게시글만 불러오기]
    @GetMapping(value = "/MyLike", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> findMyLikePostings(@RequestParam("user_seq") Long user_seq) {
        List<BoardDTO> boardDTOList = boardService.findMyLikePostingsByUserSeq(user_seq); // boardService 에서 findAllPostingsByUserId 메서드를 호출해 모든 게시글 데이터를 가져온 후 이를 boardDTOList 에 저장
        Map<String, Object> response = new HashMap<>();
        response.put("boardDTOList", boardDTOList); //response 맵에 'boardDTOList' 라는 키와 함께 값을 저장
        return ResponseEntity.ok(response);
    }

//-------------------------------------------------------------------------------------------------------

    // [#9. 키워드로 게시글 전체 조회]
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> findAllByKeyword(@RequestParam("keyword") String keyword) {
        List<BoardDTO> boardDTOSearchResult =  boardService.findAllByKeyword(keyword);
        Map<String, Object> response = new HashMap<>();
        response.put("boardDTOSearchResult", boardDTOSearchResult);
        return ResponseEntity.ok(response);
    }



//-------------------------------------------------------------------------------------------------------
//수정사항
}










