# QnA

## 1단계 - 엔티티 매핑

### 요구사항
QnA 서비스를 만들어 가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑하는지 알아본다.
- DDL을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성해 본다.
- @DataJpaTest 를 사용하여 학습 테스트를 해 본다.

### 구현할 기능 목록
- 엔티티 매핑
  - [x] answer
  - [x] delete_history
  - [x] question
  - [x] user
- 리포지토리 테스트
  - [x] answer
  - [ ] delete_history
  - [x] question
  - [x] user
