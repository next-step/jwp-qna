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

---
### 3단계 - 질문 삭제하기 리팩토링
질문 데이터를 삭제할 때에는 데이터를 완전히 삭제하는 것이 아니라 상태를 삭제 상태로 변경한다.
- [X] 질문 데이터 삭제 API 제공
- [X] 질문 데이터 삭제 API 에 대한 테스트코드 작성

로그인한 사용자와 질문한 사람이 같은 경우 삭제 할 수 있다.
- [X] 질문 데이터 삭제 시 사용자에 대한 유효성 검증 로직 추가.
- [X] 질문한 사람과 사용자가 같은 경우에만 삭제 가능한 유효성 검증 로직 테스트코드 작성

질문 데이터 삭제 시 작성된 답변이 있는 경우 질문 작성자와 답변 작성자가 동일해야만 질문을 삭제 할 수 있다.
- [X] 질문 데이터 삭제 시 답변 데이터 작성자에 대한 유효성 검증 로직 추가.
- [X] 질문 데이터 삭제 시 답변 데이터 작성자에 대한 유효성 검증 로직 테스트코드 작성 

질문 삭제 시 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태를 변경한다.
- [X] 답변 데이터 삭제 API 제공
- [X] 답변 데이터 삭제 API 에 대한 테스트코드 작성
- [X] 질문 삭제 시 답변 데이터도 삭제될 수 있도록 질문 삭제 관련 처리

질문과 답변 삭제 이력에 대한 정보를 DeleteHistory를 활용해 남긴다.
- [X] 질문과 답변 삭제에 대한 로직 수행 시 DeleteHistory를 저장한다.

Answer 도메인의 삭제 기능 리팩토링
- [X] Answer 도메인의 유효성 검증 주체를 가져온다.
- [X] 삭제에 대한 결과로 DeleteHistory 반환 

Question, Answer 도메인간의 연관관계 리팩토링
- [X] Answers 일급 컬랙션 생성 및 Question 도메인과 연관관계에 의한 연관관계 편의 메소드 처리
- [X] DeleteHistories 일급 컬랙션 생성


