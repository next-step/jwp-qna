# Step 1. 엔티티 매핑
## 요구 사항
**QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.**

- [x] 아래의 DDL(Data Definition Language)을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성해 본다.
- [x] @DataJpaTest를 사용하여 학습 테스트를 해 본다.

## 기능 및 테스트 구현 목록
- [x] Answer
  - [x] 도메인 클래스 구현
  - [x] Answer 저장 후 반환된 Id로 조회 했을 때 같은 객체가 조회 되어야 한다.
  - [x] 특정 Question에 대한 Answer 중 삭제 되지 않은 것만 조회
  - [x] 특정 AnswerId로 삭제 되지 않은 Answer 조회 했을 때 조회 결과가 있어야 한다.
  - [x] 특정 AnswerId로 삭제 된 Answer 조회 했을 때 조회 결과가 없어야 한다.
  

- [x] DeleteHistory
  - [x] 도메인 클래스 구현
  - [x] DeleteHistory 저장 후 반환된 Id로 조회 했을 때 같은 객체가 조회 되어야 한다.

  
- [x] Question
  - [x] 도메인 클래스 구현
  - [x] Question 저장 후 반환된 Id로 조회 했을 때 같은 객체가 조회 되어야 한다.
  - [x] 삭제된 상태가 아닌 Question만 조회 했을 때 조회 결과가 있어야 한다.
  - [x] 삭제된 상태가 아닌 Question을 아이디로 조회 했을 때 조회 결과가 있어야 한다.
  - [x] 삭제된 상태인 Question을 아이디로 조회 했을 때 조회 결과가 없어야 한다.

  
- [x] User
  - [x] 도메인 클래스 구현
  - [x] User 저장 후 반환된 Id로 조회 했을 때 같은 객체가 조회 되어야 한다.
  - [x] User 를 UserId로 조회 했을 때 유효한 결과가 조회 되어야 한다.
  
# Step 2. 연관 관계 매핑
## 요구 사항
QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
객체의 참조와 테이블의 외래 키를 매핑해서 객체에서는 참조를 사용하고 테이블에서는 외래 키를 사용할 수 있도록 한다.

## 도메인 관계
- Answer n : 1 Question
- Answer n : 1 User
- Question n : 1 User
- DeleteHistory n : 1 User

## 기능 및 테스트 구현 목록
- [x] Answer
  - [x] Question에 대한 연관 관계 매핑 적용
  - [x] User에 대한 연관 관계 매핑 적용
  - [x] 깨지는 기존 테스트 해결
  - [X] setDelete -> delete 변경
    - [x] 변경에 따른 연관 테스트 수정

- [x] Question
  - [x] User에 대한 연관 관계 매핑 적용
  - [x] 깨지는 기존 테스트 해결
  - [x] setDelete -> delete 변경
    - [x] 변경에 따른 연관 테스트 수정

- [x] DeleteHistory
  - [x] User에 대한 연관 관계 매핑 적용

- [x] User
  - [x] 깨지는 기존 테스트 해결
  
## Feedback 
- [x] Answer 도메인의 setQuestion 없애기
- [x] Answer, Question 도메인이 delete 메소드 실행시 DeleteHistory 인스턴스 반환하기

# Step 3. 질문 삭제하기 리팩터링
## 기능 요구 사항
- 질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태(deleted - boolean type)로 변경한다.
- 로그인 사용자와 질문한 사람이 같은 경우 삭제할 수 있다.
- 답변이 없는 경우 삭제가 가능하다.
- 질문자와 답변 글의 모든 답변자 같은 경우 삭제가 가능하다.
- 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경한다.
- 질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.
- 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory를 활용해 남긴다.

## 기능 및 테스트 구현 목록 (` : 커밋 단위)
- [ ] Question에 delete 구현 기능 `
  - [ ] 삭제시 상태를 변경하도록 구현
  - [ ] 작성자 본인만 삭제가 가능하도록 구현
  - [ ] 연관 질문이 있는 경우 삭제 하도록 구현 
  - [ ] 처리한 결과로 DeleteHistory 반환

- [ ] Answer에 delete 구현 기능 `
  - [ ] 삭제시 상태를 변경하도록 구현
  - [ ] 작성자 본인만 삭제가 가능하도록 구현
  - [ ] 처리한 결과로 DeleteHistory 반환

- [ ] QnAService 세부 구현 변경 `
  - [ ] 1.Question으로 삭제
  - [ ] 2.반환 받은 DeleteHistory를 모두 리포지토리를 통해 저장

- [ ] DeleteHistoryService에 대한 테스트 구현 `
- [ ] QnaServiceTest의 테스트 코드 리팩토링 `