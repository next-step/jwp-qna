### 3단계 요구사항
- ###`QnaService` 의 `deleteQuestion` 메소드 리팩토링 !!
- 질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태(deleted - boolean type)로 변경한다.
- 로그인 사용자와 질문한 사람이 같은 경우 삭제할 수 있다.
- 답변이 없는 경우 삭제가 가능하다.
- 질문자와 답변 글의 모든 답변자 같은 경우 삭제가 가능하다.
- 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경한다.
- 질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.
- 질문과 답변 삭제 이력에 대한 정보를 `DeleteHistory` 를 활용해 남긴다.


### 2단계 피드백
- [x] `Answer` 의 영속 상태가 `User` 의 영속 상태까지 영향을 미치는 것에 대해서는 고민
  - 아무런 영향을 끼치면 안된다고 생각해서 처음에 cascade 옵션중 detach 를 사용
  - 하지만 어떤 옵션을 주더라도 Answer 로 부터 User 의 정보가 변경됨.
    - 다른 User 로 변경이 되더라도 해당 User 의 정보를 변경하지 못하게 해야 할 것 같음
- [x] setDelete(true) 대신 delete()라고 객체에게 명확한 메시지명으로 메시지 전달
- [x] `QuestionTest` 에서  id를 지정한 이유 고민 및 테스트 독립성 지키기
- [x] `Answer` `toString` 메소드 에서 NPE 유발 방지
- [x] `AnswerRepository` questionId 를 통해서 어떻게 가져올지 고민하기.


### 궁금한 사항
- cascade 옵션 중 detach 를 사용하신 경험이 있으시다면 언제인지 알 수 있을 까요?
- sort delete 와 hard delete 는 언제 사용하는 것 일까요? 
  - sort delete 를 쓰는 경우 데이터가 방대해질 때 속도 개선이 필요한데 JPA 에서는 어떤 방식으로 개선할 수 있나요?
- @Where 절로 AndDeletedFalse 를 전부 붙이지 않아서 좋긴 하지만, 만약 Deleted 가 True 를 불러와야 하는 특정 케이스가 있다면 어떻게 해야할지 고민입니다. ㅠㅠ
- 테스트 픽스처가 어렵네요.. 이번엔 TestFactory 클래스를 만들어서 해보았는데 피드백 부탁드립니다. ㅠㅠ 
- Answer 나 Question 에서 User 의 변경을 주고 싶지 않을 때 어떻게 해야 할까요?
- 혹시, @FilterDef 를 사용해보신 경험이 있으시다면 언제인지 알 수 있을까요?