# 우아한 테크캠프 Pro 5기 - 2주차

## JPA

### 실습 환경 구축

- [QnA 저장소](https://github.com/next-step/jwp-qna)를 기반으로 미션을 진행</br>
- [온라인 코드 리뷰 요청 1단계 문서](https://github.com/next-step/nextstep-docs/blob/master/codereview/review-step1.md)를 참고해 실습 환경을
  구축

1. 미션 시작 버튼을 눌러 미션을 시작
2. 저장소에 자신의 GitHub 아이디로 된 브랜치가 생성되었는지 확인
3. 저장소를 자신의 계정으로 Fork

### 요구사항

- Entity 클래스와 Repository 클래스 작성
  - [x] Answer
  - [x] Question
  - [x] User
  - [x] DeleteHistory
- `@DataJpaTest` 를 활용한 Repository 테스트 코드 작성
  - CRUD 및 Exception 테스트
  - [x] AnswerRepositoryTest
  - [x] QuestionRepositoryTest
  - [x] UserRepositoryTest
  - [x] DeleteHistoryRepositoryTest
- 연관 관계 매핑
  - [x] Question - Answer 연관 관계 매핑
  - [x] User - Answer 연관 관계 매핑
  - [x] User - Question 연관 관계 매핑
  - [x] User - DeleteHistory 연관 관계 매핑
- 테스트 데이터 generator 생성
  - static 객체 활용 시 테스트 데이터가 공유되는 문제가 발생

### Step 3 - 리팩토링
#### 요구사항
질문 삭제
- [ ] 질문 상태 변경
  - [x] 질문 작성자와 로그인 사용자가 같은 경우
    - [x] 질문 상태를 삭제로 변경
    - [x] 질문 삭제 이력 추가
    - [x] 질문에 달린 답변의 상태를 삭제로 변경
    - [x] 답변 삭제 이력 추가
  - [x] 질문 작성자와 로그인 사용자가 다른 경우 예외 발생
  - [x] 질문 작성자와 답변 작성자가 다른 경우 예외 발생
- [ ] 리팩토링
  - [ ] `QnaService` 리팩토링
  - [ ] 일급 컬렉션 사용
### DDL
```SQL
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
);
```

```SQL
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

```SQL
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

```SQL
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

### 