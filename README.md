# QnA
## 1단계 - 엔티티 매핑

---

### 요구사항
QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
- 아래의 DDL(Data Definition Language)을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성해 본다.
- `@DataJpaTest`를 사용하여 학습 테스트를 해 본다.
```mysql
create table answer
(
  id          bigint not null auto_increment,
  contents    longtext,
  created_at  datetime(6) not null,
  deleted     bit    not null,
  question_id bigint,
  updated_at  datetime(6),
  writer_id   bigint,
  primary key (id)
) engine=InnoDB
```
```mysql
create table delete_history
(
  id            bigint not null auto_increment,
  content_id    bigint,
  content_type  varchar(255),
  create_date   datetime(6),
  deleted_by_id bigint,
  primary key (id)
) engine=InnoDB
```
```mysql
create table question
(
  id         bigint       not null auto_increment,
  contents   longtext,
  created_at datetime(6)  not null,
  deleted    bit          not null,
  title      varchar(100) not null,
  updated_at datetime(6),
  writer_id  bigint,
  primary key (id)
) engine=InnoDB
```
```mysql
create table user
(
  id         bigint      not null auto_increment,
  created_at datetime(6) not null,
  email      varchar(50),
  name       varchar(20) not null,
  password   varchar(20) not null,
  updated_at datetime(6),
  user_id    varchar(20) not null,
  primary key (id)
) engine=InnoDB

alter table user
  add constraint UK_a3imlf41l37utmxiquukk8ajc unique (user_id)
```

- [x] 엔티티 클래스 매핑
  - [x] Answer
  - [x] DeleteHistory
  - [x] Question
  - [x] User
- [x] `@DataJpaTest`를 사용한 학습 테스트
  - [x] AnswerRepositoryTest
  - [x] DeleteHistoryRepositoryTest
  - [x] QuestionRepositoryTest
  - [x] UserRepositoryTest

## 2단계 - 연관 관계 매핑

---

### 요구사항
QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
- 객체의 참조와 테이블의 외래 키를 매핑해서 객체에서는 참조를 사용하고 테이블에서는 외래 키를 사용할 수 있도록 한다.

```mysql
alter table answer
    add constraint fk_answer_to_question
        foreign key (question_id)
            references question (id)
```
```mysql
alter table answer
    add constraint fk_answer_writer
        foreign key (writer_id)
            references user (id)
```
```mysql
alter table delete_history
    add constraint fk_delete_history_to_user
        foreign key (deleted_by_id)
            references user (id)
```
```mysql
alter table question
    add constraint fk_question_writer
        foreign key (writer_id)
            references user (id)
```

- [x] Answer 연관관계 매핑
- [x] Question 연관관계 매핑
- [x] DeleteHistory 연관관계 매핑
- [x] QnaServiceTest 수정
- [x] AnswerRepositoryTest 수정
- [x] QuestionRepositoryTest 수정
- [x] UserRepositoryTest 수정
- [x] DeleteHistoryTest 수정

## 3단계 - 질문 삭제하기 리팩터링

---

### 요구사항
QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
- 질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태(deleted - boolean type)로 변경한다.
- 로그인 사용자와 질문한 사람이 같은 경우 삭제할 수 있다.
- 답변이 없는 경우 삭제가 가능하다.
- 질문자와 답변 글의 모든 답변자 같은 경우 삭제가 가능하다.
- 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경한다.
- 질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.
- 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory를 활용해 남긴다.
- `qna.service.QnaService`의 `deleteQuestion()` 메소드를 단위 테스트하기 어려운 코드와 단위 테스트 가능한 코드를 분리해 단위 테스트를 구현한다.
- 리팩터링을 완료한 후에도 `src/test/java` 디렉터리의 `qna.service.QnaServiceTest`의 모든 테스트가 통과해야 한다.

- [ ] Question
  - [ ] 질문 삭제 권한이 없을 경우 CannotDeleteException 예외 던지기
  - [ ] 답변들을 가져온다.
  - [ ] 답변이 있는지 확인한다.
  - [ ] 질문 삭제 여부를 변경한다.
- [x] Answers
  - [x] 답변이 없는지 확인한다.
  - [x] 답변을 추가한다.
  - [x] 답변을 모두 삭제한다.
  - [x] 질문자와 답변자가 모두 같은 경우 삭제가 가능하다.
- [x] Answer
  - [x] 답변 삭제 권한이 없을 경우 CannotDeleteException 예외 던지기
  - [x] 답변 삭제 여부를 변경한다.
  - [x] 답변 삭제시 답변 삭제이력을 반환한다.
- [ ] DeleteHistory
  - [ ] 질문 삭제 이력을 생성한다.
  - [x] 답변 삭제 이력을 생성한다.
- [x] User
  - [x] 생성자 인스턴스 변수 줄이기
