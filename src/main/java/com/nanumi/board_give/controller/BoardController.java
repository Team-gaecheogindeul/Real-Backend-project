package com.nanumi.board_give.controller;


import com.nanumi.board_give.dto.BoardDTO;
import com.nanumi.board_give.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    //[#1. (개인) 나눔 게시글 등록 - BoardService 클래스 호출] -----------------저장 성공-----------------
    @PostMapping(value = "/posting/sharing", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> give_posting_save(@RequestBody BoardDTO boardDTO) {
        boardService.give_posting_save(boardDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "저장 완료");
        return new ResponseEntity<>(response, HttpStatus.OK); //200 응답코드 반환
    }


//------------------------------------------------------------------------------------------------------------------

    //[#2. (타인) 나눔 게시글 전체 조회] -----------------페이징 성공----------------- (클라이언트는 요청시 page 값을 같이 param 으로 전달해야한다.)

    @GetMapping(value = "/posting/sharingAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> give_posting_findAll(@PageableDefault(size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BoardDTO> boardDTOList = boardService.give_posting_findAll(pageable);  //서비스 객체에서 조회한 데이터 여러개를 DTO 객체에 담아서 List 자료구조에 주입
        Map<String, Object> response = new HashMap<>();
        response.put("boardTotalList", boardDTOList.getContent()); //이 코드는 맵 객체에 가져온 데이터(boardDTOList)를 저장합니다. 이 때 데이터와 관련된 키("boardList")도 함께 저장합니다.
                                                                    //클라이언트에게 페이지 관련 정보 없이 실제 데이터만 전달하려면, Page.getContent() 메소드를 사용하여 content 내용만 추출하면 됩니다.
        return ResponseEntity.ok(response); //이 코드는 맵 객체를 반환합니다. 이 때 ResponseEntity.ok() 를 사용하여 HTTP 응답 코드 200(성공)과 함께 맵 객체를 반환합니다.
    }                                       // 클라이언트는 이 데이터를 JSON 형식으로 받아 사용할 수 있습니다.
    /*
    [1] Page<> 객체를 사용하는 이유
    List<>와 Page<>는 모두 여러 개의 데이터를 담을 수 있는 컬렉션 타입입니다. 
    하지만 Spring Data JPA에서 제공하는 Page<> 인터페이스는 페이징 처리에 필요한 추가적인 정보를 제공합니다.
    Page<> 객체는 데이터 뿐만 아니라, 현재 페이지 번호, 전체 페이지 수, 현재 페이지의 요소 수, 전체 요소 수 등 페이징 처리에 필요한 메타데이터도 함께 제공합니다. 
    이런 정보들은 클라이언트 측에서 페이지네이션 UI를 구현할 때 유용하게 사용될 수 있습니다.
    
    [2] Page<> 사용의 장점
    효율성: 페이징 처리를 하는 주된 이유 중 하나는 대량의 데이터를 한 번에 처리하지 않고, 작은 단위로 나누어 처리하여 시스템의 부하를 줄이기 위함입니다. 따라서 만약 대량의 데이터가 있는 상황에서 List<>로 모든 결과를 가져오면 메모리 부하가 발생할 수 있습니다. 반면에 Page<> 객체는 한 번에 일정 크기(size)만큼의 데이터만 로드하기 때문에 이러한 문제가 발생하지 않습니다.
    코드 간결성: Pageable과 함께 사용되었을 때, Page 인터페이스는 코드 작성을 간결하게 해줍니다. 복잡한 페이징 로직을 직접 구현하지 않아도 되며, 
               Spring Data JPA가 알아서 SQL 쿼리문을 생성해줍니다.
    따라서 위와 같은 이유로 Spring Data JPA에서는 페이징 처리시 Page 인터페이스를 권장합니다.
    
    [3] @PageableDefault
    @PageableDefault 어노테이션은 Pageable 객체의 여러 인스턴스 변수들의 기본값을 설정하는데 사용
    @PageableDefault 애노테이션은 기본적으로 첫 번째 페이지(page = 0)부터 데이터들을 가져오도록 설정되어 있습니다.
    만약, 다른 페이지의 데이터를 가져오고 싶다면 클라이언트 측에서 'page 파라미터를 변경'하여 요청하면 됩니다.
    예를 들어, 클라이언트가 page=1로 요청하면(2번째 페이지에 해당하는 게시물들을 모두 조회해 달라)
    Pageable pageable 객체의 page 값은 1로 설정되고 이 값을 기준으로, 두번째 페이지의 게시글들이 조회됩니다.
    그리고 이 정보가 give_posting_findAll(pageable) 메서드에 전달되어 두번째 페이지의 게시글들을 반환하게 됩니다.

    [4] pageable 인터페이스
    Pageable 인터페이스는 페이징 처리에 필요한 다양한 정보를 담을 수 있는 인스턴스 변수들을 가지고 있습니다.
    주요한 변수로는 size, sort, direction 등이 있습니다.
        size: 한 페이지에 표시할 항목의 개수를 나타냅니다.
        sort: 정렬 기준을 나타내며, 보통은 필드명을 지정합니다. 예를 들어 "board_give_id"와 같이 정렬하고자 하는 필드명을 문자열로 지정할 수 있습니다.
        direction: 정렬 방식을 나타내며, 오름차순(ASC) 또는 내림차순(DESC)으로 설정할 수 있습니다.
     */


//------------------------------------------------------------------------------------------------------------------

    //[#3. (타인,개인) 나눔 게시글 상세 조회] -----------------상세조회 성공-----------------
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

    //[#4. (개인) 나눔 게시글 전체 조회] -----------------페이징 성공-----------------
    @GetMapping(value = "/profile/sharingAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> findAllPostingsByUserSeq(@RequestParam("user_seq") Long user_seq,
// @PageableDefault(size=10, sort="id", direction=Sort.Direction.DESC)는 기본 페이지 크기를 10으로 하고, 정렬은 id 기준 내림차순으로 설정.
                                                                        @PageableDefault(size=6, sort="id", direction=Sort.Direction.DESC) Pageable pageable){
        Page<BoardDTO> boardDTOList = boardService.findAllPostingsByUserSeq(user_seq,pageable); // boardService 에서 findAllPostingsByUserId 메서드를 호출해 모든 게시글 데이터를 가져온 후 이를 boardDTOList 에 저장
        Map<String, Object> response = new HashMap<>();
        response.put("boardDTOList", boardDTOList.getContent()); //response 맵에 'boardDTOList' 라는 키와 함께 값을 저장
        return ResponseEntity.ok(response);
    }



//------------------------------------------------------------------------------------------------------------------
    /*   [#5. (개인) 나눔 게시글 상세 조회]

    => #3 과 동일한 메소드를 호출한다.
    두 개의 경로를 처리하고자 하는 경우, @GetMapping 애너테이션에서 경로를 '배열'로 설정하여 둘 이상의 경로를 지정할 수 있습니다.
    두 경로 중 하나로 호출이 들어올 때 동일한 메소드를 호출할 수 있습니다. 이렇게 하면 중복된 코드를 작성할 필요 없이 두 가지 경로를 모두 처리할 수 있습니다.  */

//------------------------------------------------------------------------------------------------------------------

    //[#6. (개인) 나눔 게시글 수정]  -----------------수정 성공----------------- (단, 클라언트는 요청시 JSON 형식 안에 게시글 번호도 같이 보내야한다.)
    @PostMapping(value = "/profile/sharing/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updatePartOfBoard(@RequestBody BoardDTO boardDTO) {
        boardService.updatePartOfBoard(boardDTO.getBoard_give_id(),boardDTO);  // 사용자가 입력한 정보를 토대로 기존 게시물의 일부 데이터를 업데이트합니다.
        Map<String, Object> response = new HashMap<>();
        response.put("message", "수정 완료");
        return new ResponseEntity<>(response, HttpStatus.OK); //200 응답코드 반환
    }
//-----------------------------------------------------------------------------------------------------


    //[#7. 좋아요/좋아요 취소 누르기]  -----------------좋아요/취소 성공-----------------

    @ResponseBody
    @PostMapping("/LikeOrNot") // '좋아요' & '좋아요 취소' 기능을 토글하기 위한 API
    public ResponseEntity<Map<String, Object>> addLikeToPost(@RequestParam("user_seq") Long user_seq, @RequestParam("board_give_id") Long board_give_id) {

        boardService.addLikeToPost(user_seq, board_give_id);
        Map<String, Object> response = new HashMap<>();
        return ResponseEntity.ok(response);
    }


//-----------------------------------------------------------------------------------------------------

    //[#8. 좋아요한 게시글만 전체 불러오기] -----------------페이징 성공-----------------
    @GetMapping(value = "/MyLike", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Object>> findMyLikePostings(@RequestParam("user_seq") Long user_seq,
                                                                 @PageableDefault(size=6, sort="id", direction=Sort.Direction.DESC)Pageable pageable){
        Page<BoardDTO> boardDTOList = boardService.findMyLikePostingsByUserSeq(user_seq,pageable); // boardService 에서 findAllPostingsByUserId 메서드를 호출해 모든 게시글 데이터를 가져온 후 이를 boardDTOList 에 저장
        Map<String, Object> response = new HashMap<>();
        response.put("boardDTOList", boardDTOList.getContent()); //response 맵에 'boardDTOList' 라는 키와 함께 값을 저장
        return ResponseEntity.ok(response);
    }

//-------------------------------------------------------------------------------------------------------

    // [#9. 키워드로 게시글 전체 조회] -----------------페이징 성공-----------------
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> findAllByKeyword(@RequestParam("keyword") String keyword,
                                                                @PageableDefault(size=6, sort="id", direction=Sort.Direction.DESC) Pageable pageable) {
        Page<BoardDTO> boardDTOSearchResult =  boardService.findAllByKeyword(keyword, pageable);
        Map<String, Object> response = new HashMap<>();
        response.put("boardDTOSearchResult", boardDTOSearchResult.getContent());
        return ResponseEntity.ok(response);
    }



//-------------------------------------------------------------------------------------------------------

}










