# JPA

## 1단계 - 엔티티 매핑

### 요구 사항

QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.

- 아래의 DDL(Data Definition Language)을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성해 본다.
- @DataJpaTest를 사용하여 학습 테스트를 해 본다.

### 스키마

#### 1. answer

```sql
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

#### 2. delete_history

```sql
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

#### 3. question

```sql
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

#### 4. user

```sql
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

### 기능 목록

- [X] Answer 엔티티 테스트를 작성한다
- [X] Answer 엔티티 클래스를 작성한다
- [X] DeleteHistory 엔티티 테스트를 작성한다
- [X] DeleteHistory 엔티티 클래스를 작성한다
- [X] Question 엔티티 테스트를 작성한다
- [X] Question 엔티티 클래스를 작성한다
- [X] User 엔티티 테스트를 작성한다
- [X] User 엔티티 클래스를 작성한다

## 2단계 - 연관 관계 맵핑

### 요구사항

QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.

객체의 참조와 테이블의 외래 키를 매핑해서 객체에서는 참조를 사용하고 테이블에서는 외래 키를 사용할 수 있도록 한다.

### 스키마

```sql
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

### 기능목록

- [ ] Answer에 Question을 연결한다
  - 하나의 질문에는 여러 답변이 달린다
- [ ] Answer에 User(작성자)를 연결한다
  - 답변 작성자는 한명이다
- [ ] DeleteHistory에 User(삭제자)를 연결한다
  - 삭제자는 한명이다
- [ ] Question에 User(작성자)를 연결한다
  - 질문 작성자는 한명이다
