### 엔티티 매핑
* [x] @DataJpaTest 를 활용한다
* [x] Answer 매핑, AnswerRepository 학습테스트
* [x] DeleteHistory 매핑, DeleteHistoryRepository 학습테스트
* [x] Question 매핑, QuestionRepository 학습테스트
* [x] User 매핑, UserRepository 학습테스트

### 연관 관계 매핑
* [x] Question으로 User를 참조할 수 있게 매핑
* [x] DeleteHistory로 User를 참조할 수 있게 매핑
* [x] Answer로 User를 참조할 수 있게 매핑
* [x] Answer로 Question을 참조할 수 있게 매핑
* [x] Question으로 Answer를 참조할 수 있게 매핑

### 질문 삭제하기 리팩터링
* [x] 모든 원시 값과 문자열을 포장하고 일급 컬렉션을 사용
* [x] 로그인 사용자와 질문한 사람이 같은지 확인
* [x] 로그인 사용자와 질문한 사람이 다르면 CannotDeleteException 발생
* [x] 답변이 없다면 삭제 상태를 true 로 변경
* [x] 답변이 있다면 질문한 사람과 같은지 확인
* [x] 답변자와 질문자가 다르면 CannotDeleteException 발생
* [x] 답변자와 질문자가 모두 같다면 Question 과  Answer 의 삭제 상태를 모두 true 로 변경
* [x] Question 과 Answer 의 삭제 이력을 DeleteHistory 를 활용해 남김
### (수정사항)질문 삭제하기 리팩터링
* [ ] QnaService 에서 User 엔티티 영속성 보장을 위해 userId를 받아 엔티티를 생성하도록 수정
* [ ] 포장객체 생성의 의도를 명확히 알 수 있도록 수정
