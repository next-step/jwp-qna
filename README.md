## JPA

### 1단계 - 엔티티 매핑
#### 요구사항
* 아래의 DDL(Data Definition Language)을 보고 유추하여 엔티티 클래스와 리포지토리 클래스 작성
* @DataJpaTest를 사용하여 학습 테스트
```roomsql
create table answer
(
    id          bigint generated by default as identity,
    contents    clob,
    created_at  timestamp not null,
    deleted     boolean   not null,
    question_id bigint,
    updated_at  timestamp,
    writer_id   bigint,
    primary key (id)
)
```
```roomsql
create table delete_history
(
    id            bigint generated by default as identity,
    content_id    bigint,
    content_type  varchar(255),
    create_date   timestamp,
    deleted_by_id bigint,
    primary key (id)
)
```
```roomsql
create table question
(
    id         bigint generated by default as identity,
    contents   clob,
    created_at timestamp    not null,
    deleted    boolean      not null,
    title      varchar(100) not null,
    updated_at timestamp,
    writer_id  bigint,
    primary key (id)
)
```
```roomsql
create table user
(
    id         bigint generated by default as identity,
    created_at timestamp   not null,
    email      varchar(50),
    name       varchar(20) not null,
    password   varchar(20) not null,
    updated_at timestamp,
    user_id    varchar(20) not null,
    primary key (id)
)

alter table user
    add constraint UK_a3imlf41l37utmxiquukk8ajc unique (user_id)
```

#### 구현 기능
- [x] 엔티티 매핑
  - [x] Answer
  - [x] DeleteHistory
  - [x] Question
  - [x] User
- [x] 중복되는 속성을 분리하는 BaseEntity 생성
- [x] 도메인 테스트 작성
  - [x] Answer
  - [x] Question
  - [x] User
- [x] 리포지토리 테스트 작성
  - [x] Answer
  - [x] DeleteHistory
  - [x] Question
  - [x] User

---

### 2단계 - 연관 관계 매핑
#### 요구사항
* 객체의 참조와 테이블의 외래 키를 매핑해서 객체에서는 참조를 사용하고 테이블에서는 외래 키를 사용할 수 있도록 한다.
* 아래의 DDL을 보고 유추한다.
```roomsql
alter table answer
    add constraint fk_answer_to_question
        foreign key (question_id)
            references question

alter table answer
    add constraint fk_answer_writer
        foreign key (writer_id)
            references user

alter table delete_history
    add constraint fk_delete_history_to_user
        foreign key (deleted_by_id)
            references user

alter table question
    add constraint fk_question_writer
        foreign key (writer_id)
            references user
```

#### 구현 기능
- [x] 연관 관계 매핑
  - [x] Answer - Question
  - [x] Answer - User
  - [x] DeleteHistory - User
  - [x] Question - User
- [x] 리포지토리 테스트 작성
  - [x] Answer - Question
  - [x] Answer - User
  - [x] DeleteHistory - User
  - [x] Question - User
- [x] 도메인 테스트 작성
  - [x] Answer - Question
  - [x] Answer - User
  - [x] DeleteHistory - User
  - [x] Question - User

---

### 3단계 - 질문 삭제하기 리팩터링
#### 요구사항
* 질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태(deleted - boolean type)로 변경한다.
* 로그인 사용자와 질문한 사람이 같은 경우 삭제할 수 있다.
* 답변이 없는 경우 삭제가 가능하다.
* 질문자와 답변 글의 모든 답변자 같은 경우 삭제가 가능하다.
* 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경한다.
* 질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.
* 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory 를 활용해 남긴다.

#### 구현 기능
- [x] 질문 삭제하기 기능
  - [x] 질문 삭제 시 삭제 상태(deleted) 업데이트
  - [x] 로그인 사용자가 질문 작성자와 동일한지 유효성 검증
  - [x] 질문과 관련된 답변 목록을 일급 컬렉션으로 구현
- [x] 답변 삭제하기 기능
  - [x] 답변 삭제 시 삭제 상태(deleted) 업데이트
  - [x] 로그인 사용자가 답변 작성자와 동일한지 유효성 검증
- [x] 삭제 이력 남기기 기능
  - [x] 질문과 답변 삭제 시 삭제 이력 저장
- [x] 질문/답변 도메인의 setDeleted 제거
- [x] 질문 삭제 서비스 리팩터링
