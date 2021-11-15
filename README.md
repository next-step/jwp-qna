### 2단계 피드백
- [ ] cascade 전략 고민
- [ ] `Answer` 의 영속 상태가 `User` 의 영속 상태까지 영향을 미치는 것에 대해서는 고민
- [x] setDelete(true) 대신 delete()라고 객체에게 명확한 메시지명으로 메시지 전달
- [ ] `QuestionTest` 에서  id를 지정한 이유 고민 및 테스트 독립성 지키기
- [x] `Answer` `toString` 메소드 에서 NPE 유발 방지
- [ ] `AnswerRepository` questionId 를 통해서 어떻게 가져올지 고민하기.
  ```이런식으로도 가능합니다.
    List<Answer> findByQuestionAndDeletedFalse(Question question);
    
    ---------
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    
    @Column(name = "question_id", insertable = false, updatable = false)
    private Long questionId;
    물론 기술적으로 가능한 것과 언제 쓰고 언제 쓰지 말아야할지는 고민해야봐할 부분이기 때문에
    참고만하시고 상황에 맞게 잘 선택해서 사용하시면 될 것 같네요.
    ------
    뒤에 반복적으로 붙는 AndDeletedFalse 부분도 @Where 애너테이션을 통해 줄일 수 있으니 공부해보시면 좋을 것 같습니다!```
    

