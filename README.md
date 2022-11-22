# 우아한테크캠프 PRO - JPA 미션
<pre>
작성자: 강덕문 / https://github.com/deokmoon / deok2moon@gmail.com
작성시작일: 2022.11.07
</pre>

## Step1 엔티티 매핑 요구사항 정리
* 아래의 DDL(Data Definition Language)을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성
* @DataJpaTest를 사용하여 학습 테스트
### Answer Entity
~~~~sql
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
~~~~
### DeleteHistory Entity
~~~~sql
create table delete_history
(
    id            bigint generated by default as identity,
    content_id    bigint,
    content_type  varchar(255),
    create_date   timestamp,
    deleted_by_id bigint,
    primary key (id)
)
~~~~
### Question Entity
~~~~sql
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
~~~~
### User Entity
~~~~sql
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
~~~~

### 적용한 yml 내용
~~~~yml
spring:
  datasource:
    url: jdbc:h2:~/test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    username: sa
    driver-class-name: org.h2.Driver
  h2:
    console:
      enabled: true
  jpa:
    hibernate:
      ddl-auto: create-drop
    properties:
      hibernate:
        show_sql: true
        format_sql: true

logging.level:
  org.hibernate.SQL: debug
  org.hibernate.type: trace
~~~~
* Mysql 형식으로 설정
* H2 콘솔 설정 진행
* ddl-auto를 create으로 설정
  * 테스트 환경으로 로그 확인하기 위함 
  * application 실행 시 기존 테이블 삭제되고 생성 로그 확인
* BaseDateEntity 적용
  * 공통적인 created_at / updated_at 처리
* domain / repository 패키지 부리
* Test 
  * repository: 조회 저장 삭제 위주의 테스트코드 작성

## Step2 연관 관계 매핑
* 하나의 User는 Question을 여러번 올릴 수 있다.
* 하나의 User는 Answer를 여러번 답변할 수 있다.
* 하나의 User는 DeleteHistory를 여러개 가질 수 있다.
* 하나의 Question엔 여러개의 Answer가 생길 수 있다.

## step1, step2 회고
* jpa에서 많이 사용되는 cascadeType, @JoinColumn의 동작방식을 살펴보게 되는 계기가 되었다.
* 자주 사용하는 어노테이션이라도 동작방식을 한번 생각해봐야겠다.
* test fixture 관련하여 이번 미션 리뷰어 근환님이 알려주신 FixtureMonkey 라이브러리 참고해야겠다.
* 추가로 test fixture 만들 때 아래와 같이 만드는 방식을 고려해야겠다.
  * Answer.create(writer, question, "contents");
  * new Answer(writer, question, "contents");
## step3 질문 삭제하기 리팩토링 
###기능 요구사항
* 삭제는 데이터의 상태를 삭제 상태(deleted - boolean type)로 변경
* '로그인사용자'와 '질문한사람'이 같은 경우 삭제 가능
* 답변이 없는 경우 삭제 가능
* 질문을 삭제할 때 답변 또한 삭제(상태 변경)
* 질문자와 답변자가 다른 경우 답변을 삭제 불가
* 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory를 활용하여 남김
### 프로그래밍 요구사항
* qna.service.QnaService의 deleteQuestion()는 질문 삭제 기능을 구현한 코드
* 단위 테스트하기 어려운 코드와 단위 테스트 가능한 코드를 분리해 단위 테스트 가능한 코드에 대해 단위 테스트를 구현
* 리팩터링을 완료한 후에도 qna.service.QnaServiceTest의 모든 테스트가 통과 해야 함
### Step3 회고
* 시간에 쫓기는 느낌으로 구현하다보니, 깔끔하지 못한 코드가 발생함
  * 이번 미션 리뷰어이신 근환님의 리뷰로 보기 좋은 코드를 작성할 수 있었음(특히 삭제기능)
* 이번 미션을 통해 특히 배운 것은 JPA뿐만 아니라 test fixture를 어떻게 구현하고, 프로덕트코드와 테스트코드를 격리할지에 대해 꺠닫게 됨