# JPA

## 1단계 - 엔티티 매핑
### 요구사항
- [x] 엔티티 클래스, 리포지토리 클래스 생성
  - [x] Answer
  - [x] Question
  - [x] User
  - [x] DeleteHistory
- [x] DataJpaTest를 사용한 테스트를 코드 작성
  - [x] AnswerRepositoryTest
  - [x] QuestionRepositoryTest
  - [x] UserRepositoryTest
  - [x] DeleteHistoryRepositoryTest

### 2단계 - 연관 관계 매핑
- [x] Answer -> Question refactor
- [x] Answer -> User refactor
- [x] DeleteHistory-> User refactor
- [x] Question-> User refactor
- [x] 테스트 코드 리팩토링
  - [x] DeleteHistoryRepository 테스트
  - [x] UserRepository 테스트
  - [x] AnswerRepository 테스트
  - [x] QuestionRepository 테스트
- [x] equals method, hashcode override 
