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
- cascade 옵션 중 detach 를 사용하는 때는 언제일까?
- sort delete 와 hard delete 는 언제 사용하는 것 일까?
- @Where 절로 AndDeletedFalse 를 전부 붙이지 않아서 좋긴 하지만, 만약 Deleted 가 True 를불러와야 하는 특정 케이스가 있다면 어떻게 해야할까?
- 테스트 픽스처가 어렵네요.. static 변수를 두는 것 이 맞을지 고민입니다. 
- Answer 나 Question 에서 User 의 변경을 주고 싶지 않을 때 어떻게 해야 할까요?