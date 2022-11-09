## 요구사항 정리

3단계 - 질문삭제하기 리펙토링

### 엔티티 맵핑
- [X] 요구사항 쿼리를 보고 엔티티와 맵핑시키는 작업을 진행한다
- [X] 요구사항 쿼리를 보고 레포지토리 클래스를 작성해본다
- [X] `@DataJpaTest`를 이용하여 테스트를 진행한다
- [X] UserRepository 테스트 코드 작성
- [X] DeleteHistoryRepository 테스트 코드 작성
- [X] QuestionRepository 테스트 코드 작성
- [X] AnswerRepository 테스트 코드 작성
### 연관관계 맵핑
주어진 요구사항 DDL을 보고 연관관계 맵핑을 진행한다.
- [X] Answer 연관관계 맵핑
- [X] User 연관관계 맵핑
- [X] DeleteHistory 연관관계 맵핑
- [X] Question 연관관계 맵핑
- [X] Answer 연관관계 맵핑에 따른 테스트 코드 수정
- [X] User 연관관계 맵핑에 따른 테스트 코드 수정
- [X] DeleteHistory 연관관계 맵핑에 따른 테스트 코드 수정
- [X] Question 연관관계 맵핑에 따른 테스트 코드 수정
### 질문삭제하기 리팩토링
- [X] Question 상태를 삭제 상태(deleted - boolean type)로 변경
- [ ] 로그인 사용자와 질문한 사람이 같은 경우에만 삭제가 가능
- [ ] 답변이 없는 경우 삭제가 가능
- [ ] 질문자와 답변 글의 모든 답변자 같은 경우 삭제가 가능
- [ ] 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경
- [ ] 질문자와 답변자가 다른 경우 답변을 삭제가 불가능하다
- [ ] 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory에 데이터를 INSERT한다