# 요구사항

## 1단계

- 아래의 DDL 을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성하고 `@DataJpaTest`를 사용하여 학습 테스트를 작성해본다.
    - [ ] 답변
        - [x] 엔티티 매핑
        - [ ] 학습 테스트 작성
    - [ ] 삭제 기록
        - [ ] 엔티티 매핑
        - [ ] 학습 테스트 작성
    - [ ] 질문
        - [ ] 엔티티 매핑
        - [ ] 학습 테스트 작성
    - [ ] 사용자
        - [ ] 엔티티 매핑
        - [ ] 학습 테스트 작성

```mysql
create table answer
(
    id          bigint      not null auto_increment,
    contents    longtext,
    created_at  datetime(6) not null,
    deleted     bit         not null,
    question_id bigint,
    updated_at  datetime(6),
    writer_id   bigint,
    primary key (id)
) engine = InnoDB;

create table delete_history
(
    id            bigint not null auto_increment,
    content_id    bigint,
    content_type  varchar(255),
    create_date   datetime(6),
    deleted_by_id bigint,
    primary key (id)
) engine = InnoDB;

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
) engine = InnoDB;

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
) engine = InnoDB;

alter table user
    add constraint UK_a3imlf41l37utmxiquukk8ajc unique (user_id)
```
