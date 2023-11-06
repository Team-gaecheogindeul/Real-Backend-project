# 개발환경
1. IDE: IntelliJ IDEA Community
2. Spring Boot 2.7.14
3. jar(packaging)
4. JDK 11
5. mysql 8.0.34(커뮤니티,나눔게시글) & Firebase(로그인 및 채팅기능)
   ## mysql DataBase 계정 생성 및 권한 부여
   ```
   create database db_codingrecipe;
   create user user_codingrecipe@localhost identified by '1234';
   grant all privileges on db_codingrecipe.* to user_codingrecipe@localhost;
   ```
6. Spring Data JPA
7. Thymeleaf

# 게시판 주요기능 (나눠요)
1. 나눔 게시글 등록(post) [전체 : (/posting/sharing)]
2. 나눔 게시글 전체조회(get) [전체 : (/posting/sharingAll), 개인 : (/profile/sharingAll/{user_seq})]  
3. 나눔 게시글 상세조회(get) [전체 : (/posting/sharing/{board_give_id},{user_seq}) / 개인 : (/profile/sharingAll/{board_give_id},{user_seq})]
4. 나눔 게시글 수정(post) [개인 : (/profile/sharingAll/update/{board_give_id})]
    - 개인 게시글 상세조회 에서 수정 버튼 클릭 
    - 서버에서 해당 게시글의 기존 정보를 조회 해줌 
    - 제목, 내용 수정 등 입력 받아서 서버로 요청 
    - 수정 처리 
5. 나눔 게시글 삭제(delete) [개인 : (/profile/sharing/delete/{board_give_id})]
6. 좋아요/좋아요 취소(post) [개인 : (/LikeOrNot/{board_give_id},{user_seq})]
7. 좋아요한 게시글 전체조회(get) [개인 : (/MyLike/{user_seq})]
8. 키워드로 게시글 전체조회(get) [전체 :(/search/{keyword})]
9. 인기게시글 전체조회(get) [전체 : (/posting/sharingAll/Likes)]
10. 추천게시판 목록 조회 (추천 알고리즘) [개인 : (/giverecommend/{user_seq})]
    특정 사용자에게 추천되는 게시판의 목록을 반환하는 것입니다.
    사용자의 고유 번호("user_seq")를 받아 이를 기반으로 추천 게시판 목록을 생성하고, 이를 반환합니다.

* 페이징처리 : [@PageableDefault 애노테이션은 사용]

* 파일(이미지)첨부하기 [인코딩된 문자열을 프론트 서버로 부터 받아 문자열 변수에 저장] 
   - 단일 파일 첨부 [BoardDTO 클래스 :  private List<String> imageUrls; 인스턴스 변수 이용]
   - 다중 파일 첨부 [BoardDTO 클래스 :  private List<String> imageUrls; 인스턴스 변수 이용]

# 커뮤니티 주요기능

1. 커뮤니티 게시글 등록(post) [전체 : (/{type}Posting/)]
2. 커뮤니티 게시글 전체조회(get) [전체 : (/{type}posting/All), 개인 : (/{type}profile/ALL/{user_seq})]
3. 커뮤니티 게시글 상세조회(get) [전체 : (/{type}posting/{board_id},{user_seq}) / 개인 : (/{type}profile/{board_id},{user_seq})]
4. 커뮤니티 게시글 수정(post) [개인 : (/{type}profile/update/{board_id})]
   - 개인 게시글 상세조회 에서 수정 버튼 클릭
   - 서버에서 해당 게시글의 기존 정보를 조회 해줌
   - 제목, 내용 수정 등 입력 받아서 서버로 요청
   - 수정 처리
5. 커뮤니티 게시글 삭제(delete) [개인 : (/{type}profile/{board_id})]
6. 좋아요/좋아요 취소(post) [개인 : (/{type}posting/All/LikeOrNot/{board_id},{user_seq})]
7. 좋아요한 게시글 전체조회(get) [개인 : (/{type}MyLike/{user_seq})]
8. 키워드로 게시글 전체조회(get) [전체 :(/{type}posting/All/search/{keyword})]
9. 인기게시글 전체조회(get) [전체 : (/{type}posting/All/Likes)]
10. 댓글 입력(post) [(/{type}Posting/Comment)]
11. 대댓글 입력(post) [(/{type}Posting/ChildComment/{comment_id})]
12. 댓글 조회(get) [전체 : (/{type}Posting/Comment/{board_id}), 개인 : (/{type}profile/Comment/{board_id})]
13. 댓글 수정(put) [개인 : (/{type}Posting/Comment)]
14. 대댓글 수정(put) [개인 : (/{type}Posting/ChildComment/{comment_id},{user_seq})]
15. 댓글 삭제(delete) [개인 : (/{type}Posting/Comment/{comment_id})]
16. 대댓글 삭제(delete) [개인 : (/{type}Posting/ChildComment/{comment_id},{user_seq})]
17. 추천게시판 목록 조회 (추천 알고리즘) [개인 : (/{type}communityrecommend/{user_seq})]
    특정 사용자에게 추천되는 게시판의 목록을 반환하는 것입니다.
    사용자의 고유 번호("user_seq")를 받아 이를 기반으로 추천 게시판 목록을 생성하고, 이를 반환합니다.

* 페이징처리 : [@PageableDefault 애노테이션은 사용]

* 파일(이미지)첨부하기 [인코딩된 문자열을 프론트 서버로 부터 받아 문자열 변수에 저장]
   - 단일 파일 첨부 [CommunityDTO 클래스 :  private List<String> boardImageUrls; 인스턴스 변수 이용]
   - 다중 파일 첨부 [CommunityDTO 클래스 :  private List<String> boardImageUrls; 인스턴스 변수 이용]

# 운영 환경
AWS - Lightsail 인스턴스 사용

* AWS LightSail의 장점
   ◦ 간단함: LightSail은 간단한 웹 애플리케이션을 호스팅 하는데 적합합니다.
            간단한 웹사이트, 개인 프로젝트, 소규모 어플리케이션 등을 위해 설계되었습니다.
   ◦ 비용 효율성: LightSail은 일정 비용으로 정해진 리소스를 제공 받을 수 있어 예상 가능한 비용으로 프로젝트를 진행할 수 있다는 것을 의미합니다.
   ◦ 관리의 용이성: LightSail은 사용자 친화적인 관리 인터페이스를 제공하며, 서버 설정과 관련된 복잡한 부분들을 대부분  자동화하여 제공합니다.

* AWS LightSail의 한계
   ◦ 확장성 한계: EC2와 비교하면, LightSail 인스턴스는 성능 및 확장성 면에서 제한적입니다. 복잡하거나 대규모 트래픽이 예상되는 애플리케이션에 
                대해서는 EC2가 더 나은 선택일 수 있습니다.**
   => 따라서 책 나눔 애플리케이션 같은 경우 초기 단계에서는 트래픽도 많지 않고 관리도 간단하기 때문에 
      AWS Lightsail을 이용하는 것도 좋은 선택일 수 있습니다. 하지만 애플리케이션이 성장하고 사용자수가 많아짐에 따라서 
      확장성과 세부적인 설정 등의 필요성이 생긴다면 그때 AWS EC2로 옮기는 것도 고려해볼 만 합니다.**

# 서비스 소개

   참고서를 주요 무료 도서 나눔 대상으로 하되, 추가적으로 입시 정보를 학생들끼리 공유 할 수 있는 커뮤니티 서비스이다.

# 서비스 배경
   때때로, 이사를 하거나 방을 정리할 때 부피를 많이 차지하는 책을 처분해야 할 상황이 생기게 된다. 
   이때 책을 버리거나 중고 거래 사이트에 판매하는 등의 활동으로 책을 처분하는 경우가 대부분이다. 
   또한, 책을 기부하려고 해도 접근성이 편리한 경로를 찾기 어렵다. 이러한 책들을 필요한 사람들에게 나눔을 통해 전달된다면 사회적 효용을 
   증진 시킬 수 있다. 특히 책 기부를 통해 기초생활수급자, 한 부모가정 등 취약계층 사람들의 경제적 부담을 덜어줄 수 있다.

   기존의 책 나눔은 지역 도서관에서 오프라인 행사로만 행해지고 있다. 그렇기에 지역 주민 행사에 참여할 수 있는 경우가 많다 
   그리고 기간이 정해져 있어 언제든지 책을 기부할 수 없는 한계가 있다. 이를 위한 대책으로 우리 팀은 나눔만을 지원하는 서비스인 
   ‘책 나누미‘를 기획하게 되었다.

# 서비스 목적
1. 비용 절감
   정부의 교육비 지원 사업은 주로 학비나 교과서 구매 등의 비용부담을 줄일 수 있으나,‘책 나누미’ 어플과 같은 무료 책 나눔
   커뮤니티가 있다면 도서 구매에 대한 지출을 줄여 절감한 비용을 다른 책을 구매하거나, 다양한 학습활동에 활용할 수 있다.

2. 정부 지원의 한계 보완
   정부에서 운영하는 교육비 지원 사업은 기준 중위소득 50% 초과 70% 미만의 가정의 학생들만 교육비 지원사업의 혜택 대상자로 한정되며 
   이에 한에서만 지원. 해당 소득 범위가 아닌 학생들 중 여러 가정의 경제상황에 따라 책 구매 비용이 부담스러운 학생 또한 존재 이들 또한 
   해당 서비스를 활용하여 책을 나눔 받을 수 있다. 추가적으로 연 1회에 지급되는 금액의 많고 적음은 쉽게 판단할 수 없는 부분이기 때문에, 
   ‘사용기간’ 관점에서 보면 2023년 시행안 기준 보통 3월에 신청하여 8월 말까지 배정받은 교육급여를 모두 사용해야 하는 조건있다.
   그렇기 때문에 해당 기간 이외있어서, 필요로 하는 도서 구매 수요를 충족시키는데 한계가 있음이 분명하다.

3. 나눔 문화에 대한 접근성 향상
   책을 처분하려는 사람 또는 기부를 하고 싶은 사람들에게 책을 나눔 할 수 있는 접근성 좋은 창구 역할을 수행가능, 특히 오프라인의
   각 지역 곳곳의 교육기관에서  무료 도서 나눔 행사를 진행 하는 모습을 찾아 볼 수 있는데, 이러한 행사들은 주로 해당 교육기관 내에서만 
   진행 되다보니, 시공간적 한계는 물론이거니와, 나눔 되는 도서의 카테고리도 한정적 일수 밖에 없다.
   따라서 에듀쉐어는 이러한 시간/공간적 제약을 해소하는 역할을 할 수 있다. 이를 통해 기부에 대한 소극적인 문화를 개선하고 
   커뮤니티 활성화를 통해 나눔 문화를 형성 가능하다.











