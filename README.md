# JPA

## Step1. 엔티티 매핑 요구사항

- QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.

### answer 테이블

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

### delete_history 테이블

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

### question 테이블

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

### user

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

## Step1. 엔티티 매핑 기능명세서

- [X] DDL(Data Definition Language)을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성해 본다.
    - [X] answer 엔티티 클래스, 레포지토리 클래스 작성
    - [X] delete_history 엔티티 클래스, 레포지토리 클래스 작성
    - [X] question 엔티티 클래스, 레포지토리 클래스 작성
    - [X] user 엔티티 클래스, 레포지토리 클래스 작성
- [X] @DataJpaTest를 사용하여 학습 테스트를 해 본다.
    - [X] answer 엔티티의 학습테스트를 해 본다.
    - [X] delete_history 엔티티의 학습테스트를 해 본다.
    - [X] question 엔티티의 학습테스트를 해 본다.
    - [X] user 엔티티의 학습테스트를 해 본다.

* 힌트 및 추가 요구사항

- [X] 프로퍼티를 변경하여 좀 더 고도화된 JPA 개발을 준비한다.
    - [X] Spring Data JPA 사용 하여 동작 쿼리를 로그로 확인할 수 있게 한다.
    - [X] MySQL Dialect를 사용한다.
- [X] BaseEntity를 활용하여 엔티티의 통일된 동작을 추가한다.

## Step2. 연관관계 매핑 요구사항

- QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.

* 객체의 참조와 테이블의 외래 키를 매핑해서 객체에서는 참조를 사용하고 테이블에서는 외래 키를 사용할 수 있도록 한다.

### 아래의 DDL을 보고 연관관계를 유추하라

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
            references user )
alter table user
    add constraint UK_a3imlf41l37utmxiquukk8ajc unique (user_id)
```

## Step2. 연관관계 매핑 기능명세서

- QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.


- [ ] 연관관계 매핑
    - [ ] Answer와 Question의 다대일 양방향 연관관계를 설정한다.
        - [ ] Question이 생성/변경될때 Answer도 생성/변경할 수 있다. (Cascade.ALL)
        - [ ] Question이 삭제되면 Answer도 삭제된다. (Cascade.ALL)
    - [ ] Answer와 User의 다대일 양방향 연관관계를 설정한다.
        - [ ] User가 생성/변경될때 Answer도 생성/변경할 수 있다. (Cascade.PERSIST, Cascade.MERGE)
        - [ ] User가 삭제되어도 Answer는 삭제되지 않는다.
    - [ ] DeleteHistory와 User의 다대일 양방향 연관관계를 설정한다.
        - [ ] User가 생성/변경될때 DeleteHistory도 생성/변경할 수 있다. (Cascade.ALL)
        - [ ] User가 삭제되면 DeleteHistory도 삭제된다. (Cascade.ALL)
    - [ ] Question과 User의 다대일 양방향 연관관계를 설정한다.
        - [ ] User가 생성/변경될때 Question도 생성/변경할 수 있다. (Cascade.PERSIST, Cascade.MERGE)
        - [ ] User가 삭제되어도 Question은 삭제되지 않는다.
- [ ] 레포지토리 테스트 코드 수정
    - [ ] AnswerRepository 테스트
        - [ ] Answer 저장 테스트
        - [ ] Answer 수정 테스트
        - [ ] Answer 삭제 테스트
        - [ ] Answer 조회시 LazyLoading 검증
        - [ ] Answer 조회시 Question 정보도 조회
        - [ ] Answer 조회시 User 정보도 조회
    - [ ] DeleteHistoryRepository 테스트
        - [ ] DeleteHistory 저장 테스트
    - [ ] QuestionRepository 테스트
        - [ ] Question 저장 테스트
        - [ ] Question을 조회하면 연관된 Answer도 조회된다.
        - [ ] Question을 삭제하면 Answer도 삭제된다.
    - [ ] UserRepository 테스트
        - [ ] User를 조회하면 연관된 Answer/Question/DeleteHistory 모두 지연 로딩이 된다.
        - [ ] User를 삭제하면 DeleteHistory도 삭제되는지 테스트