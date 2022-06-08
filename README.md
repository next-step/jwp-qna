# JPA

## Step1. 엔티티 매핑

### 기능 요구사항

- DDL(Data Definition Language)을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성해 본다.
- @DataJpaTest를 사용하여 학습 테스트를 해 본다.


- [x] 생성일/수정일 상속을 위한 공통 엔티티 생성

- [x] answer 테이블
    - [x] Entity 클래스 작성
    - [x] Repository 클래스 작성
    - [x] Entity 테스트코드 작성

- [x] delete_history 테이블
    - [x] Entity 클래스 작성

- [x] question 테이블
    - [x] Entity 클래스 작성
    - [x] Repository 클래스 작성
    - [x] Entity 테스트코드 작성

- [x] user 테이블
    - [x] Entity 클래스 작성
    - [x] Repository 클래스 작성
    - [x] Entity 테스트코드 작성

---

## Step2. 연관 관계 매핑

### 기능 요구사항

- 객체의 참조와 테이블의 외래 키를 매핑해서 객체에서는 참조를 사용하고 테이블에서는 외래 키를 사용할 수 있도록 한다.


- [x] 테이블 구조, 모델 설계 확인 및 개선점 도출
- [x] 엔티티 간 연관관계 수정