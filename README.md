# QNA JPA

## 기능 구현 목록

### 엔티티 매핑

- [x] 기본 소스 분석
- [x] DDL 분석
- [x] Jpa Auditing 기능 추가 (createAt, updateAt)
- [x] 엔티티 클래스 매핑
    - [x] Id column -> auto increment
    - [x] clob(longtext) -> column definition으로 지정 (내용이니까..)
    - [x] not null 과 같은 column definition 확인
- [x] Repository 클래스 작성
    - [x] 이미 작성되어 있음
- [x] DataJpaTest 활용한 테스트 케이스 작성
    - [x] CRUD 케이스에 대해 모두 작성

### 연관관계 매핑

- [x] 연관관계 파악
    - [x] Answer <-> question 다대일,일대다 양방향
    - [x] Answer -> user 다대일 단방향
    - [x] DeleteHistory -> user 다대일 단방향
    - [x] Question -> user 다대일 단방향
- [x] 연관 관계 테스트
    - [x] Question <-> answer
    - [x] Question <-> user
    - [x] Answer <-> user
    - [x] deleteHistory <-> user





