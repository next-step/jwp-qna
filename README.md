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
  * [x] Answer - Question 연관 관계 매핑
  * [x] Answer - User 연관 관계 매핑
  * [x] DeleteHistory - User 연관 관계 매핑
  * [x] Question - User 연관 관계 매핑

### 3단계 - 질문 삭제하기 리팩터링
#### 기능 요구사항
* 질문 삭제
  * [ ] 질문 데이터를 삭제 시 완전 삭제가 아닌 데이터 상태를 (deleted - boolean type)으로 변경
  * [ ] 로그인 사용자와 질문한 사람이 같은 경우 삭제 가능
  * [ ] 답변이 존재하지 않는 경우만 삭제 가능
  * [ ] 질문자와 모든 답변자가 가능 경우 삭제 가능
* 답변 삭제
  * [ ] 질문 삭제 시 답변도 같이 삭제 상태(deleted)를 변경
  * [ ] 질문자와 답변자가 다른 경우 삭제 불가능
* 삭제 이력
  * [ ] 질문과 답변 삭제 이력에 대한 정보를 삭제 이력을 활용해 기록 
#### 프로그래밍 요구사항
* [ ] qna.service.QnaService의 deleteQuestion() 질문 삭제 기능메소드를 
단위 테스트 가능한 부분과 어려운 부분을 분리하여 테스트 가능한 부분에 대해 TDD 구현
* [ ] 리팩터링을 완료한 후에도 src/test/java 디렉터리의 qna.service.QnaServiceTest의 모든 테스트가 통과








