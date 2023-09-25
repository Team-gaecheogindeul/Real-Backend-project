package com.nanumi.community.controller;


import com.nanumi.community.dto.CommentDTO;
import com.nanumi.community.dto.CommunityDTO;
import com.nanumi.community.entity.CommentEntity.CommentEntity;
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
import java.util.Optional;

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
    @PostMapping(value = "/{type}Posting/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> PostingSave(@PathVariable String type, @RequestBody CommunityDTO communityDTO) {
        switch (type) {
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

    //[#2. 게시글 (커뮤니티 별) 전체 조회] (클라이언트는 요청시 page 값을 같이 param 으로 전달해야한다.)

    @GetMapping(value = "/{type}posting/All", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> postingFindAll(@PathVariable String type, @PageableDefault(size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CommunityDTO> communityDTOList; // switch 문 이전에 선언
        /*
         Page<CommunityDTO> communityDTOList 가 선언되었지만 초기화되지 않았기 때문에 컴파일 에러가 발생할 수 있다.
         따라서 초기에 null 값으로 처리해주고, null 값에 대한 처리구문을 아래에 입력해주자.
         */
        switch (type) {
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
    //[#3. 게시글 상세 조회] -----------------상세조회 성공-----------------
    @GetMapping(value = {"/{type}posting", "/{type}profile"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> postingFindById(@PathVariable String type, @RequestParam("board_id") Long board_id) {
        Optional<CommunityDTO> optionalCommunityDTO; // 초기화하지 않음

    /*
     Page<CommunityDTO> communityDTOList 가 선언되었지만 초기화되지 않았기 때문에 컴파일 에러가 발생할 수 있다.
     따라서 초기에 null 값으로 처리해주고, null 값에 대한 처리구문을 아래에 입력해주자.
     */

        switch (type) {
            case "Free":
                optionalCommunityDTO = freeService.postingFindById(board_id);  //서비스 객체에서 조회한 데이터 여러개를 DTO 객체에 담아서 List 자료구조에 주입
                break;
            case "Learn":
                optionalCommunityDTO = learnService.postingFindById(board_id);
                break;
            case "School":
                optionalCommunityDTO = schoolService.postingFindById(board_id);
                break;
            case "College":
                optionalCommunityDTO = collegeService.postingFindById(board_id);
                break;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }

        if (optionalCommunityDTO.isPresent()) {
            CommunityDTO communityDTO = optionalCommunityDTO.get();
            Map<String, Object> response = new HashMap<>();
            response.put("communityDTO", communityDTO); //response 에 Key 값 으로 boardDTO 를 저장하고, boardDTO 객체를 value 값으로 주입
            return ResponseEntity.ok(response); //200응답 코드와 같이 출력
        } else { //Optional<BoardDTO> 객체 내부에 BoardDTO 객체가 존재하지 않는 경우
            return ResponseEntity.notFound().build(); //404 응답 코드 반환
        }
    }


//------------------------------------------------------------------------------------------------------------------

    //[#4. (개인) 게시글 (커뮤니티 별) 전체 조회] -----------------페이징 성공-----------------
    @GetMapping(value = "/{type}profile/ALL", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> postingFindAll(@RequestParam("user_seq") String user_seq, @PathVariable String type,
                                                              @PageableDefault(size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CommunityDTO> communityDTOList; // switch 문 이전에 선언
        /*
         Page<CommunityDTO> communityDTOList 가 선언되었지만 초기화되지 않았기 때문에 컴파일 에러가 발생할 수 있다.
         따라서 초기에 null 값으로 처리해주고, null 값에 대한 처리구문을 아래에 입력해주자.
         */
        switch (type) {
            case "Free":
                communityDTOList = freeService.findAllPostingsByUserSeq(user_seq, pageable); //서비스 객체에서 조회한 데이터 여러개를 DTO 객체에 담아서 List 자료구조에 주입
                break;
            case "Learn":
                communityDTOList = learnService.findAllPostingsByUserSeq(user_seq, pageable);
                break;
            case "School":
                communityDTOList = schoolService.findAllPostingsByUserSeq(user_seq, pageable);
                break;
            case "College":
                communityDTOList = collegeService.findAllPostingsByUserSeq(user_seq, pageable);
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
    /*   [#5. (개인) 게시글 (커뮤니티 별) 상세 조회]

    => #3 과 동일한 메소드를 호출한다.
    두 개의 경로를 처리하고자 하는 경우, @GetMapping 애너테이션에서 경로를 '배열'로 설정하여 둘 이상의 경로를 지정할 수 있습니다.
    두 경로 중 하나로 호출이 들어올 때 동일한 메소드를 호출할 수 있습니다. 이렇게 하면 중복된 코드를 작성할 필요 없이 두 가지 경로를 모두 처리할 수 있습니다.  */

//------------------------------------------------------------------------------------------------------------------

    //[#6. 게시글 (커뮤니티 별)수정]  -----------------수정 성공----------------- (단, 클라언트는 요청시 JSON 형식 안에 게시글 번호도 같이 보내야한다.)
    @PostMapping(value = "/{type}profile/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updatePartOfBoard(@RequestBody CommunityDTO communityDTO,@PathVariable String type,
                                                    @RequestParam("board_id") Long board_id) {

        switch (type) {
            case "Free": // 사용자가 입력한 정보를 토대로 기존 게시물의 일부 데이터를 업데이트합니다.
                freeService.updatePartOfBoard(board_id, communityDTO);
                break;
            case "Learn":
                learnService.updatePartOfBoard(board_id, communityDTO);
                break;
            case "School":
                schoolService.updatePartOfBoard(board_id, communityDTO);
                break;
            case "College":
                collegeService.updatePartOfBoard(board_id, communityDTO);
                break;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("message", "수정 완료");
        return new ResponseEntity<>(response, HttpStatus.OK); //200 응답코드 반환
    }
//-----------------------------------------------------------------------------------------------------


    //[#7. (커뮤니티별) 좋아요/좋아요 취소 누르기]  -----------------좋아요/취소 성공-----------------

    @PostMapping("/{type}posting/All/LikeOrNot") // '좋아요' & '좋아요 취소' 기능을 토글하기 위한 API
    public ResponseEntity<Map<String, Object>> addLikeToCommunityPost(@RequestParam("user_seq") String user_seq, @RequestParam("board_id") Long board_id, @PathVariable String type) {
        switch (type) {
            case "Free":
                freeService.addLikeToPost(user_seq, board_id);
                break;
            case "Learn":
                learnService.addLikeToPost(user_seq, board_id);
                break;
            case "School":
                schoolService.addLikeToPost(user_seq, board_id);
                break;
            case "College":
                collegeService.addLikeToPost(user_seq, board_id);
                break;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }

        Map<String, Object> response = new HashMap<>();
        return new ResponseEntity<>(response, HttpStatus.OK); //200 응답코드 반환
    }


// -----------------------------------------------------------------------------------------------------

    //[#8. 좋아요한 게시글만 (커뮤니티별) 전체 불러오기] -----------------페이징 성공-----------------
    @GetMapping(value = "/{type}MyLike", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Object>> findMyLikePostings(@RequestParam("user_seq") String user_seq, @PathVariable String type,
                                                                 @PageableDefault(size=6, sort="id", direction=Sort.Direction.DESC)Pageable pageable){
        Page<CommunityDTO> communityDTOList;

        switch (type) {
            case "Free":
                communityDTOList = freeService.findMyLikePostingsByUserSeq(user_seq,pageable); // boardService 에서 findAllPostingsByUserId 메서드를 호출해 모든 게시글 데이터를 가져온 후 이를 boardDTOList 에 저장
                break;
            case "Learn":
                communityDTOList = learnService.findMyLikePostingsByUserSeq(user_seq,pageable); // boardService 에서 findAllPostingsByUserId 메서드를 호출해 모든 게시글 데이터를 가져온 후 이를 boardDTOList 에 저장
                break;
            case "School":
                communityDTOList = schoolService.findMyLikePostingsByUserSeq(user_seq,pageable); // boardService 에서 findAllPostingsByUserId 메서드를 호출해 모든 게시글 데이터를 가져온 후 이를 boardDTOList 에 저장
                break;
            case "College":
                communityDTOList = collegeService.findMyLikePostingsByUserSeq(user_seq,pageable); // boardService 에서 findAllPostingsByUserId 메서드를 호출해 모든 게시글 데이터를 가져온 후 이를 boardDTOList 에 저장
                break;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }


        Map<String, Object> response = new HashMap<>();
        response.put("communityDTOList", communityDTOList.getContent()); //response 맵에 'boardDTOList' 라는 키와 함께 값을 저장
        return ResponseEntity.ok(response);
    }

//-------------------------------------------------------------------------------------------------------

    // [#9. 키워드로 (커뮤니티별) 게시글 전체 조회] -----------------페이징 성공-----------------
    @GetMapping(value = "/{type}posting/All/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> findAllByKeyword(@RequestParam("keyword") String keyword, @PathVariable String type,
                                                                @PageableDefault(size=6, sort="id", direction=Sort.Direction.DESC) Pageable pageable) {
    Page<CommunityDTO> communityDTOSearchResult;

    switch (type) {
    case "Free":
        communityDTOSearchResult = freeService.findAllByKeyword(keyword, pageable); //서비스 객체에서 조회한 데이터 여러개를 DTO 객체에 담아서 List 자료구조에 주입
        break;
    case "Learn":
        communityDTOSearchResult = learnService.findAllByKeyword(keyword, pageable);
        break;
    case "School":
        communityDTOSearchResult = schoolService.findAllByKeyword(keyword, pageable);
        break;
    case "College":
        communityDTOSearchResult = collegeService.findAllByKeyword(keyword, pageable);
        break;
    default:
        throw new IllegalArgumentException("Invalid type: " + type);
    }
        if (communityDTOSearchResult == null) {
        throw new RuntimeException("No data found for type: " + type); // 또는 다른 적절한 예외 처리
    }//서비스 호출 후에도 값이 없다면 RuntimeException 을 발생시킨다. 이렇게 하면 컴파일 에러 없이 안전하게 코드를 실행할 수 있다.

    Map<String, Object> response = new HashMap<>();
    response.put("CommunityDTOSearchResult", communityDTOSearchResult.getContent());
    return ResponseEntity.ok(response);
    }

//-------------------------------------------------------------------------------------------------

    //[#10. 인기게시글 (커뮤니티 별) 전체 조회] (클라이언트는 요청시 page 값을 같이 param 으로 전달해야한다.)

    @GetMapping(value = "/{type}posting/All/Likes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> postingFindAllByLikes(@PathVariable String type, @PageableDefault(size = 6, sort = "likeCount", direction = Sort.Direction.DESC) Pageable pageable) {
/*
    sort 속성값을 "likeCount"로 변경하였습니다. 이것은 결과를 'likeCount' 필드 값에 따라 정렬하도록 지시합니다. 또한 direction 속성값을 Sort.Direction.DESC로 설정하여 내림차순 정렬을 적용했습니다.
    이러한 변경으로 인해 각 게시판 타입(Free, Learn, School, College)에 대해 좋아요 개수가 가장 많은 게시글부터 반환됩니다.

 */
        Page<CommunityDTO> communityDTOList; // switch 문 이전에 선언
        /*
         Page<CommunityDTO> communityDTOList 가 선언되었지만 초기화되지 않았기 때문에 컴파일 에러가 발생할 수 있다.
         따라서 초기에 null 값으로 처리해주고, null 값에 대한 처리구문을 아래에 입력해주자.
         */
        switch (type) {
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


    //-------------------------------------------------------------------------------------------------
    //[#11. 댓글 입력] - 해당 게시글 번호를 ( board_id ) 프론트 에서 입력해주어야한다.
    @PostMapping(value = "/{type}Posting/Comment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> CommentSave(@PathVariable String type, @RequestBody CommentDTO commentDTO) {
        switch (type) {
            case "Free":
                freeService.CommentSave(commentDTO);
                break;
            case "Learn":
                learnService.CommentSave(commentDTO);
                break;
            case "School":
                schoolService.CommentSave(commentDTO);
                break;
            case "College":
                collegeService.CommentSave(commentDTO);
                break;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("message", "댓글 저장 완료");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //-------------------------------------------------------------------------------------------------
    //[#12. 대댓글 입력] - 클라이언트에서 대댓글을 작성할 때, 해당 대댓글이 어느 댓글에 속하는지를 알 수 있도록 부모 댓글의 ID(CommentId)를 함께 전달해야 한다.
    @PostMapping(value = "/{type}Posting/ChildComment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> ChildCommentSave(@PathVariable String type, @RequestParam("comment_id") Long comment_id, @RequestBody CommentDTO commentDTO) {

        switch (type) {
            case "Free":
                freeService.ChildCommentSave(comment_id, commentDTO);
                break;
            case "Learn":
                learnService.ChildCommentSave(comment_id, commentDTO);
                break;
            case "School":
                schoolService.ChildCommentSave(comment_id, commentDTO);
                break;
            case "College":
                collegeService.ChildCommentSave(comment_id, commentDTO);
                break;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("message", "대댓글 저장 완료");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //-------------------------------------------------------------------------------------------------
    //[#13. 댓글 조회 & 게시글 상세조회(개인, 타인)와 같이 호출 되어야 한다.]
//    @GetMapping(value = {"/{type}posting/Comment", "/{type}profile/Comment"}, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Map<String, Object>> commentFindById(@PathVariable String type, @RequestParam("board_id") Long board_id) {
//
//        Optional<CommentDTO> optionalCommentDTO; // 초기화하지 않음
//
//    /*
//     Page<CommunityDTO> communityDTOList 가 선언되었지만 초기화되지 않았기 때문에 컴파일 에러가 발생할 수 있다.
//     따라서 초기에 null 값으로 처리해주고, null 값에 대한 처리구문을 아래에 입력해주자.
//     */
//
//        switch (type) {
//            case "Free":
//                optionalCommentDTO = freeService.commentFindById(board_id);  //서비스 객체에서 조회한 데이터 여러개를 DTO 객체에 담아서 List 자료구조에 주입
//                break;
//            case "Learn":
//                optionalCommentDTO = learnService.commentFindById(board_id);
//                break;
//            case "School":
//                optionalCommentDTO = schoolService.commentFindById(board_id);
//                break;
//            case "College":
//                optionalCommentDTO = collegeService.commentFindById(board_id);
//                break;
//            default:
//                throw new IllegalArgumentException("Invalid type: " + type);
//        }
//
//        if (optionalCommentDTO.isPresent()) {
//            CommentDTO commentDTO = optionalCommentDTO.get();
//            Map<String, Object> responseComment = new HashMap<>();
//            responseComment.put("CommentDTO", commentDTO); //response 에 Key 값 으로 commentDTO 를 저장하고, boardDTO 객체를 value 값으로 주입
//            return ResponseEntity.ok(responseComment); //200응답 코드와 같이 출력
//        } else { //Optional<BoardDTO> 객체 내부에 BoardDTO 객체가 존재하지 않는 경우
//            return ResponseEntity.notFound().build(); //404 응답 코드 반환
//        }
//    }


    //-------------------------------------------------------------------------------------------------
    //[#13. 댓글 수정(커뮤니티별) & 게시글 수정과 같이 호출 되어야 한다.]

//    @PostMapping(value = "/{type}profile/update/Comment", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Object> updatePartOfComment(@RequestBody CommentDTO commentDTO,@PathVariable String type,
//                                                    @RequestParam("board_id") Long board_id, @RequestParam("user_seq") String user_seq) {
//
//        switch (type) {
//            case "Free": // 사용자가 입력한 정보를 토대로 기존 게시물의 일부 데이터를 업데이트합니다.
//                freeService.updatePartOfComment(board_id, user_seq, commentDTO);
//                break;
//            case "Learn":
//                learnService.updatePartOfComment(board_id, commentDTO);
//                break;
//            case "School":
//                schoolService.updatePartOfComment(board_id, commentDTO);
//                break;
//            case "College":
//                collegeService.updatePartOfComment(board_id, commentDTO);
//                break;
//            default:
//                throw new IllegalArgumentException("Invalid type: " + type);
//        }
//        Map<String, Object> response = new HashMap<>();
//        response.put("message", "댓글 수정 완료");
//        return new ResponseEntity<>(response, HttpStatus.OK); //200 응답코드 반환
//    }
}
















