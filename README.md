## JPA
### 1단계 - 엔티티 매핑 
#### 기능 요구사항
* 객체와 테이블 매핑
  * [x] Answer
  * [x] DeleteHistory
  * [x] Question
  * [x] User
* @DataJpaTest를 활용하여 테스트 진행
  * [x] AnswerRepository
  * [x] DeleteHistoryRepository
  * [x] QuestionRepository
  * [x] UserRepository

### 2단계 - 연관 관계 매핑
#### 기능 요구사항
* DDL을 유추해서 연관 관계 매핑
  * [ ] Answer - User 연관 관계 매핑
  * [ ] Answer - User 연관 관계 매핑
  * [ ] DeleteHistory - User 연관 관계 매핑
  * [ ] Question - User 연관 관계 매핑