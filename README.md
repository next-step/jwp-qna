# JPA

---

## 🚀 1단계 - 엔티티 매핑

### 요구 사항
- DDL(Data Definition Language)을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성
- `@DataJpaTest`를 사용하여 학습 테스트

### DDL
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
### 테이블 분석

- answer

| 컬럼명 | 데이터 타입 | not null | pk | 기타 |
| --- | --- | --- | --- | --- |
| id | bigint |  | O | generated by default as identity |
| contents | clob |  |  |  |
| created_at | timestamp | O |  |  |
| deleted | boolean | O |  |  |
| question_id | bigint |  |  |  |
| updated_at | timestamp |  |  |  |
| writer_id | bigint |  |  |  |
- delete_history

| 컬럼명 | 데이터 타입 | not null | pk | 기타  |
| --- | --- | --- | --- | --- |
| id | bigint |  | O | generated by default as identity |
| content_id | bigint |  |  |  |
| content_type | varchar(255) |  |  |  |
| create_date | timestamp |  |  |  |
| deleted_by_id | bigint |  |  |  |
- question

| 컬럼명 | 데이터 타입 | not null | pk | 기타 |
| --- | --- | --- | --- | --- |
| id | bigint |  | O | generated by default as identity |
| contents | clob |  |  |  |
| created_at | timestamp | O |  |  |
| deleted | boolean | O |  |  |
| title | varchar(100) | O |  |  |
| updated_at | timestamp |  |  |  |
| writer_id | bigint |  |  |  |
- user

| 컬럼명 | 데이터 타입 | not null | pk | 기타  |
| --- | --- | --- | --- | --- |
| id | bigint |  | O | generated by default as identity |
| created_at | timestamp | O |  |  |
| email | varchar(50) |  |  |  |
| name | varchar(20) | O |  |  |
| password | varchar(20) | O |  |  |
| updated_at | timestamp |  |  |  |
| user_id | varchar(20) |  |  | unique |

### 테스트
- DataJpaTest를 사용한 테스트 작성
  - Answer
    - [x] 생성자 Exception 테스트
    - [x] writer 확인 테스트
    - [x] Question 변경 테스트
  - AnswerRepository
    - [x] 등록, 조회, 삭제
    - [x] Question Id로 조회
    - [x] Id로 조회
  - Question
    - [x] Question작성자 변경이 가능하다.
  - QuestionRepository
    - [x] 등록, 조회, 삭제
    - [x] 삭제되지 않은 목록 조회
    - [x] id로 삭제되지 않은 Question 조회
  - User
    - [x] update Exception 테스트
    - [x] 이름, 이메일 비교
  - UserRepository
    - [x] 등록, 조회, 삭제
    - [x] userId로 조회
  - DeleteHistory
  - DeleteHistoryRepository
    - [x] 등록, 조회, 삭제
