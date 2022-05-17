## JPA
### 단계별 기능목록 정의

---
### 1단계 - 엔티티 매핑
제시된 도메인 객체에 대한 엔티티 매핑 진행
- [X] User 엔티티 매핑
- [X] Question 엔티티 매핑
- [X] Answer 엔티티 매핑
- [X] DeleteHistory 엔티티 매핑
- [X] 매핑된 엔티티에 대한 DDL 이 정상적으로 처리되는지 확인

@DataJpaTest 를 활용한 도메인 테스트 진행
- [X] User 엔티티 테스트코드 작성
- [X] Question 엔티티 테스트코드 작성
- [X] Question 엔티티의 삭제되지 않은 목록을 조회하는 API에 대한 테스트 코드 작성
- [X] Answer 엔티티 테스트코드 작성
- [X] Answer 엔티티의 findByIdAndDeletedFalse API 에 대한 테스트 코드 작성
- [X] Answer 엔티티의 findByQuestionIdAndDeletedFalse API 에 대한 테스트 코드 작성

---
### 2단계 - 연관 관계 매핑
- [X] Question 엔티티 연관관계 매핑, 외래키 연결
- [X] Answer 엔티티 연관관계 매핑, 외래키 연결
- [X] DeleteHistory 연관관계 매핑, 외래키 연결