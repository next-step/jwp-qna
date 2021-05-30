## 1단계 요구사항

* 아래의 DDL에 맞는 엔티티를 설계한다.

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

create table delete_history
(
    id            bigint generated by default as identity,
    content_id    bigint,
    content_type  varchar(255),
    create_date   timestamp,
    deleted_by_id bigint,
    primary key (id)
)

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

* User, Question, Answer 순으로 엔티티를 매핑한다.
* 각각의 엔티티 매핑을 진행하며 테스트케이스를 작성한다.

###1단계 질문

Q. 유저 삭제 테스트케이스를 userRepository.delete(JAVAJIGI) 를 실행하여 단독 테스트케이스 실행시에 성공을 했었는데요. 전체 테스트케이스 실행에서는 실패가 되었습니다.  
다른 테스트케이스 실행이 영향을 끼칠 수 있을거 같아서 @BeforeEach 애노테이션으로 userRepository.deleteAll()을 실행하게 하였는데 마찬가지로 테스트케이스는 실패하였습니다.  
원인이 무엇일까요?

Q. 삭제 테스트케이스 실행 시 userRepository.save(*JAVAJIGI*); 를 실행시키면 예상과 달리 id가 2L로 들어가는 것을 확인하였는데요. (deleteById를 구현 하여 테스트를 진행해보았었습니다) 이 이유는 무엇일까요? 앞선 질문의 원인과 비슷한 원인일까요?