# STEP1. 엔티티 맵핑 

## 요구사항

- QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
- @DataJpaTest를 사용하여 학습 테스트를 해 본다.


### 테이블 구조 
```mysql
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

## 주요 기능 이해 

|어노테이션|설명|옵션|
|:---:|:---:|:---:|
|@DataJpaTest|엔티티를 테스트|
|@AutoConfigureTestDatabase|내장된 임베디드 데이터베이스|Replace.Any|
|@AutoConfigureTestDatabase|@ActiveProfiles 설정한 환경값에따라 데이터소스가 결정됨|Replace.NONE|
|@ActiveProfiles("...")|액티브 설정|"test","dev"|
|@Size| 저장하기 전에 데이터의 검증 절차를 한번 더 갖기 때문에 더 안전하고 강력한 접근을 한다|

### 힌트 
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.show-sql=true

### 무엇을 이해했는가? 

1. Entity는 영속성 컨텍스트에서 보관하고 있다.
2. 트랜잭션이 끝나는 시점에 나간다고 했는데 `save`가 먼저 나간 이유가 무엇인가? 
   1. JPA는 먼저 query를 실행함으로써 IDENTITY를 생성해야하기에 flush 단계에 다가지 않고 먼저 나가게 된다.

3. 왜 조회가 먼저가 되었는가? 
   1. `merge`작업을 하기위해서 내가 존재하는 녀석인지 확인하기 위해서 작동했다.
    
4. flush 무엇인가? 
   1. 이때 쓰기 지연 저장소에 쌓아 놨던 INSERT, UPDATE, DELETE SQL들이 DB에 날라간다
      주의! 영속성 컨텍스트를 비우는 것이 아니다.


## 분석
1. Answer
   1. 객체를 생성 
      1. 유저가 null인지 확인한다
      2. 유저가 답변을 다는 Question이 존재하는지 확인한다.
   2. 필수 값은 무엇인가? 
      1. questionId
      2. writerId
2. Question
   1. 객체를 생성
   2. 기능
      1. writerBy
         1. 유저가 null인지 체크 
      2. AddAnswer
3. DeleteHistory
   1. 생성시 현재시간 
   2. 수정은 불가능(createDate) 
4. User
   1. 기능 
      1. update
         1. 아이디와 패스워드가 다른 경우 오류 
         2. 이름, 이메일 변경 
      2. equalsNameAndEmail
         1. 이름과 이메일이 동일한지 체크
      3. 유저를 게스트로 생성시 
         1. 게스트 여부 존재