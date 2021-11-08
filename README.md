#  2주차 - JPA

---

# 1단계 : 엔티티 매핑

### 1. DDL(Data Definition Language)을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성한다.
- [X] Answer 엔티티 클래스 작성
- [X] DeleteHistory 엔티티 클래스 작성
- [X] Question 엔티티 클래스 작성
- [X] User 엔티티 클래스 작성
- [X] 각 엔티티 별 리포지토리 클래스 확인

### 2. @DataJpaTest 를 사용하여 학습 테스트를 작성한다.
- [X] User 엔티티 테스트 코드 작성
- [X] Question 엔티티 테스트 코드 작성
- [X] Answer 엔티티 테스트 코드 작성

---

# 2단계 : 연관 관계 매핑

### 1. 객체의 참조와 테이블의 외래 키를 매핑해서 객체에서는 참조를 사용하고 테이블에서는 외래 키를 사용할 수 있도록 한다.
- [x] Question 엔티티 연관관계 및 외래 키 매핑
- [X] Answer 엔티티 연관관계 및 외래 키 매핑
- [X] DeleteHistory 연관관계 및 외래 키 매핑

# 3단계 : 질문 삭제하기 리팩터링

### 1. 질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태(deleted - boolean type)로 변경한다.
- [X] Question 삭제 시, deleted 필드를 true 로 설정
- [X] Question 삭제 조건
    - [X] 로그인 사용자와 Question 의 Writer 가 같은 경우 삭제가능
    - [X] Question 의 Writer 와 모든 Answer 의 Writer 가 같은 경우 삭제가 가능하다.
    - [X] Answer 가 없는 경우 삭제가능

### 2. 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경한다.
- [ ] Answer 삭제 시, deleted 필드를 true 로 설정
- [ ] Answer 삭제 조건
    - [ ] Question 의 Writer 와 Answer 의 Writer 가 같으면 삭제 가능
    - [ ] Question 의 Writer 와 Answer 의 Writer 가 다른 경우 Answer 삭제 불가능

### 3. Question 과 Answer 삭제 이력에 대한 정보를 DeleteHistory 를 활용해 남긴다.
- [ ] Question 삭제 시, DeleteHistory 에 추가
- [ ] Answer 삭제 시, DeleteHistory 에 추가
