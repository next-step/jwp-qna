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
- [ ] Answer
  - [x] Question에 대한 연관 관계 매핑 적용
  - [x] User에 대한 연관 관계 매핑 적용
  - [x] 깨지는 기존 테스트 해결


- [ ] Question
  - [ ] User에 대한 연관 관계 매핑 적용
  - [ ] 깨지는 기존 테스트 해결

- [ ] DeleteHistory
  - [ ] User에 대한 연관 관계 매핑 적용