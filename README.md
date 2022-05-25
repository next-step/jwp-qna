### todo

- [x] answer 엔티티 jpa 적용
- [x] delete_history 엔티티 jpa 적용
- [x] question 엔티티 jpa 적용
- [x] user 엔티티 jpa 적용
- [x] user repository test
- [x] answer repository test
- [x] question repository test
- [x] delete_history repository test

- [ ] answer와 question 연관 관계
- [ ] answer와 user 연관 관계
- [ ] delete_history와 user 연관 관계
- [ ] question과 user 연관 관계

### 요구사항

- step2 QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.

```
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

- step1 객체의 참조와 테이블의 외래 키를 매핑해서 객체에서는 참조를 사용하고 테이블에서는 외래 키를 사용할 수 있도록 한다.

- QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.

- 아래의 DDL(Data Definition Language)을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성해 본다.
- @DataJpaTest를 사용하여 학습 테스트를 해 본다.

```
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

```
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

```
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

```
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