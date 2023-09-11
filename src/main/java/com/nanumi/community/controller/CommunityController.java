package com.nanumi.community.controller;

import com.nanumi.community.dto.CommunityDTO;
import com.nanumi.community.service.CollegeService;
import com.nanumi.community.service.FreeService;
import com.nanumi.community.service.LearnService;
import com.nanumi.community.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController //이렇게 하면 모든 메서드는 기본적으로 @ResponseBody 가 있는 것처럼 동작해 반환 값이 JSON 으로 변환됩니다.
@RequiredArgsConstructor
public class CommunityController {

     /* [게시판 4개] 기본 리소스
                   자유 : FreePosting
                   학습 : LearnPosting
                   고등학교 입시 : SchoolPosting
                   대학교 입시 : CollegePosting
      */

    //[Service 객체 선언]
    private final FreeService freeService;
    private final LearnService learnService;
    private final SchoolService schoolService;
    private final CollegeService collegeService;

    //[#1. 게시글 등록 ] -저장 완료-
    @PostMapping(value = "/{type}Posting", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> PostingSave(@PathVariable String type, @RequestBody CommunityDTO communityDTO) {
        switch(type){
            case "Free":
                freeService.PostingSave(communityDTO);
                break;
            case "Learn":
                 learnService.PostingSave(communityDTO);
                break;
            case "School":
                 schoolService.PostingSave(communityDTO);
                break;
            case "College":
                 collegeService.PostingSave(communityDTO);
                break;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("message", "저장 완료");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



//------------------------------------------------------------------------------------------------------------------

    //[#2. 게시글 전체 조회] -----------------페이징 성공----------------- (클라이언트는 요청시 page 값을 같이 param 으로 전달해야한다.)

    @GetMapping(value = "/{type}posting/All", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> postingFindAll(@PathVariable String type,@PageableDefault(size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CommunityDTO> communityDTOList = null; // switch 문 이전에 선언
        /*
         Page<CommunityDTO> communityDTOList 가 선언되었지만 초기화되지 않았기 때문에 컴파일 에러가 발생할 수 있다.
         따라서 초기에 null 값으로 처리해주고, null 값에 대한 처리구문을 아래에 입력해주자.
         */
        switch(type){
            case "Free":
                communityDTOList = freeService.postingFindAll(pageable);  //서비스 객체에서 조회한 데이터 여러개를 DTO 객체에 담아서 List 자료구조에 주입
                break;
            case "Learn":
                communityDTOList = learnService.postingFindAll(pageable);
                break;
            case "School":
                communityDTOList = schoolService.postingFindAll(pageable);
                break;
            case "College":
                communityDTOList = collegeService.postingFindAll(pageable);
                break;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
        if (communityDTOList == null) {
            throw new RuntimeException("No data found for type: " + type); // 또는 다른 적절한 예외 처리
        }//서비스 호출 후에도 값이 없다면 RuntimeException 을 발생시킨다. 이렇게 하면 컴파일 에러 없이 안전하게 코드를 실행할 수 있다.

        Map<String, Object> response = new HashMap<>();
        response.put("CommunityDTOList", communityDTOList.getContent()); //이 코드는 맵 객체에 가져온 데이터(boardDTOList)를 저장한다. 이 때 데이터와 관련된 키("boardList")도 함께 저장.
        //클라이언트에게 페이지 관련 정보 없이 실제 데이터만 전달하려면, Page.getContent() 메소드를 사용하여 content 내용만 추출하면 된다.
        return ResponseEntity.ok(response); //이 코드는 맵 객체를 반환합니다. 이 때 ResponseEntity.ok() 를 사용하여 HTTP 응답 코드 200(성공)과 함께 맵 객체를 반환한다.
    }                                       // 클라이언트는 이 데이터를 JSON 형식으로 받아 사용할 수 있다.


//------------------------------------------------------------------------------------------------------------------
//    //[#3. 게시글 상세 조회] -----------------상세조회 성공-----------------
//    @GetMapping(value = {"/posting/sharing", "/profile/sharing"}, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Map<String, Object>> give_posting_findById(@RequestParam("board_give_id") Long board_give_id)  {
//        Optional<BoardDTO> optionalBoardDTO = boardService.give_posting_findById(board_give_id);
//        if (optionalBoardDTO.isPresent()) { //Optional<BoardDTO> 객체 내부에 BoardDTO 객체가 존재하는 경우
//            BoardDTO boardDTO = optionalBoardDTO.get();
//            Map<String, Object> response = new HashMap<>();
//            response.put("boardDTO", boardDTO); //response 에 Key 값 으로 boardDTO 를 저장하고, boardDTO 객체를 value 값으로 주입
//            return ResponseEntity.ok(response); //200응답 코드와 같이 출력
//        } else { //Optional<BoardDTO> 객체 내부에 BoardDTO 객체가 존재하지 않는 경우
//            return ResponseEntity.notFound().build(); //404 응답 코드 반환
//        }
//    }
//
////------------------------------------------------------------------------------------------------------------------
//
//    //[#4. (개인) 나눔 게시글 전체 조회] -----------------페이징 성공-----------------
//    @GetMapping(value = "/profile/sharingAll", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Map<String, Object>> findAllPostingsByUserSeq(@RequestParam("user_seq") Long user_seq,
//// @PageableDefault(size=10, sort="id", direction=Sort.Direction.DESC)는 기본 페이지 크기를 10으로 하고, 정렬은 id 기준 내림차순으로 설정.
//                                                                        @PageableDefault(size=6, sort="id", direction=Sort.Direction.DESC) Pageable pageable){
//        Page<BoardDTO> boardDTOList = boardService.findAllPostingsByUserSeq(user_seq,pageable); // boardService 에서 findAllPostingsByUserId 메서드를 호출해 모든 게시글 데이터를 가져온 후 이를 boardDTOList 에 저장
//        Map<String, Object> response = new HashMap<>();
//        response.put("boardDTOList", boardDTOList.getContent()); //response 맵에 'boardDTOList' 라는 키와 함께 값을 저장
//        return ResponseEntity.ok(response);
//    }
//
//
//
////------------------------------------------------------------------------------------------------------------------
//    /*   [#5. (개인) 나눔 게시글 상세 조회]
//
//    => #3 과 동일한 메소드를 호출한다.
//    두 개의 경로를 처리하고자 하는 경우, @GetMapping 애너테이션에서 경로를 '배열'로 설정하여 둘 이상의 경로를 지정할 수 있습니다.
//    두 경로 중 하나로 호출이 들어올 때 동일한 메소드를 호출할 수 있습니다. 이렇게 하면 중복된 코드를 작성할 필요 없이 두 가지 경로를 모두 처리할 수 있습니다.  */
//
////------------------------------------------------------------------------------------------------------------------
//
//    //[#6. (개인) 나눔 게시글 수정]  -----------------수정 성공----------------- (단, 클라언트는 요청시 JSON 형식 안에 게시글 번호도 같이 보내야한다.)
//    @PostMapping(value = "/profile/sharing/update", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Object> updatePartOfBoard(@RequestBody BoardDTO boardDTO) {
//        boardService.updatePartOfBoard(boardDTO.getBoard_give_id(),boardDTO);  // 사용자가 입력한 정보를 토대로 기존 게시물의 일부 데이터를 업데이트합니다.
//        Map<String, Object> response = new HashMap<>();
//        response.put("message", "수정 완료");
//        return new ResponseEntity<>(response, HttpStatus.OK); //200 응답코드 반환
//    }
////-----------------------------------------------------------------------------------------------------
//
//
//    //[#7. 좋아요/좋아요 취소 누르기]  -----------------좋아요/취소 성공-----------------
//
//    @ResponseBody
//    @PostMapping("/LikeOrNot") // '좋아요' & '좋아요 취소' 기능을 토글하기 위한 API
//    public ResponseEntity<Map<String, Object>> addLikeToPost(@RequestParam("user_seq") Long user_seq, @RequestParam("board_give_id") Long board_give_id) {
//
//        boardService.addLikeToPost(user_seq, board_give_id);
//        Map<String, Object> response = new HashMap<>();
//        return ResponseEntity.ok(response);
//    }
//
//
////-----------------------------------------------------------------------------------------------------
//
//    //[#8. 좋아요한 게시글만 전체 불러오기] -----------------페이징 성공-----------------
//    @GetMapping(value = "/MyLike", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Map<String,Object>> findMyLikePostings(@RequestParam("user_seq") Long user_seq,
//                                                                 @PageableDefault(size=6, sort="id", direction=Sort.Direction.DESC)Pageable pageable){
//        Page<BoardDTO> boardDTOList = boardService.findMyLikePostingsByUserSeq(user_seq,pageable); // boardService 에서 findAllPostingsByUserId 메서드를 호출해 모든 게시글 데이터를 가져온 후 이를 boardDTOList 에 저장
//        Map<String, Object> response = new HashMap<>();
//        response.put("boardDTOList", boardDTOList.getContent()); //response 맵에 'boardDTOList' 라는 키와 함께 값을 저장
//        return ResponseEntity.ok(response);
//    }
//
////-------------------------------------------------------------------------------------------------------
//
//    // [#9. 키워드로 게시글 전체 조회] -----------------페이징 성공-----------------
//    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Map<String, Object>> findAllByKeyword(@RequestParam("keyword") String keyword,
//                                                                @PageableDefault(size=6, sort="id", direction=Sort.Direction.DESC) Pageable pageable) {
//        Page<BoardDTO> boardDTOSearchResult =  boardService.findAllByKeyword(keyword, pageable);
//        Map<String, Object> response = new HashMap<>();
//        response.put("boardDTOSearchResult", boardDTOSearchResult.getContent());
//        return ResponseEntity.ok(response);
//    }
//
//
//
////-------------------------------------------------------------------------------------------------------

}














