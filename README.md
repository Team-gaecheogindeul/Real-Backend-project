# 개발환경
1. IDE: IntelliJ IDEA Community
2. Spring Boot 2.7.14
3. jar(packaging)
4. JDK 11
5. mysql 8.0.34
6. Spring Data JPA
7. Thymeleaf

# 게시판 주요기능 (나눠요)
1. 게시글 등록 [나눔 : (/posting/sharing)]
2. 게시글 전체조회 [타인 : (/posting/sharingAll), 개인 : (/profile/sharingAll)]  
3. 게시글 상세 조회 [타인 : (/posting/sharing/{board_give_id}) / 개인 : (/profile/sharingAll/){board_give_id}]
4. 글수정 [/profile/sharingAll/update]
    - 개인 게시글 상세조회 에서 수정 버튼 클릭 
    - 서버에서 해당 게시글의 기존 정보를 조회 해줌 
    - 제목, 내용 수정 등 입력 받아서 서버로 요청 
    - 수정 처리 
5. 글삭제(/board/delete/{id})
6. 페이징처리 : 프론트(ios) 영역
7. 파일(이미지)첨부하기 
   - 단일 파일 첨부
   - 다중 파일 첨부
   - 파일 첨부와 관련하여 추가될 부분들  
     - save.html  
     - BoardDTO  
     - BoardService.save()  
     - BoardEntity
     - BoardFileEntity, BoardFileRepository 추가
     - detail.html
   - github에 올려놓은 코드를 보시고 어떤 부분이 바뀌는지 잘 살펴봐주세요. 

    - board_table(부모) - board_file_table(자식)

# 게시판 주요기능 (받아요)


```



create table board_table
(
id             bigint auto_increment primary key,
created_time   datetime     null,
updated_time   datetime     null,
board_contents varchar(500) null,
board_hits     int          null,
board_pass     varchar(255) null,
board_title    varchar(255) null,
board_writer   varchar(20)  not null,
file_attached  int          null
);

create table board_file_table
(
id                 bigint auto_increment primary key,
created_time       datetime     null,
updated_time       datetime     null,
original_file_name varchar(255) null,
stored_file_name   varchar(255) null,
board_id           bigint       null,
constraint FKcfxqly70ddd02xbou0jxgh4o3
    foreign key (board_id) references board_table (id) on delete cascade
);
```










## mysql DataBase 계정 생성 및 권한 부여 
```
create database db_codingrecipe;
create user user_codingrecipe@localhost identified by '1234';
grant all privileges on db_codingrecipe.* to user_codingrecipe@localhost;
```