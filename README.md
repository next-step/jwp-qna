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
