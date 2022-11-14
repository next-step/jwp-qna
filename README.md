# QnA
## 요구 사항
* QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
* DDL을 보고 유추하여 엔티티 클래스와 레포지토리 클래스를 작성해 본다.
* @DataJpaTest를 사용하여 학습 테스트를 해 본다.
* 객체의 참조와 테이블의 외래 키를 매핑해서 객체에서는 참조를 사용하고 테이블에서는 외래 키를 사용할 수 있도록 한다.
* 질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태로 변경한다.
* 로그인 사용자와 질문한 사람이 같은 경우 삭제할 수 있다.
* 답변이 없는 경우 삭제가 가능하다
* 질문자와 답변 글의 모든 답변자가 같은 경우 삭제가 가능하다.
* 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태를 변경한다.
* 질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.
* 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory를 활용해 남긴다.
* qna.service.QnaService의 deleteQuestion()는 앞의 질문삭제 기능을 구현한 코드이다. 이 메서드는 단위테스트 하기 어려운 코드와 단위 테스트 가능한 코드가 섞여 있다.
* 단위 테스트하기 어려운 코드와 단위 테스트 가능한 코드를 분리해 단위 테스트 가능한 코드에 대해 단위 테스트를 구현한다.
* 리팩터링을 완료한 후에도 src/test/java 디렉터리의 qna.serviceQnaServiceTest의 모든 테스트가 통과해야 한다.

alter table answer
  add constraint fk_answer_to_question
  foreign key (question_id)
  references question

alter table answer
add constraint fk_answer_writer
foreign key (writer_id)
references user

alter table delete_history
add constraint fk_delete_history_to_user
foreign key (deleted_by_id)
references user

alter table question
add constraint fk_question_writer
foreign key (writer_id)
references user


## 진행상황
- [ ] Answer Entity 구현
- [ ] DeleteHistory Entity 구현
- [ ] Question Entity 구현
- [ ] User Entity 구현
- [ ] Entity 구현 주어진 DDL스펙과 일치하는지 확인
- [ ] AnswerTest 구현 (저장 후 찾기 테스트, 저장 후 수정 테스트, 저장 후 삭제 테스트)
- [ ] QuestionTest 구현 (저장 후 찾기 테스트, 저장 후 수정 테스트, 저장 후 삭제 테스트)
- [ ] UserTest 구현 (저장 후 찾기 테스트, 저장 후 수정 테스트)
- [ ] Answer Entity 연관관계 추가
- [ ] DeleteHistory Entity 연관관계 추가
- [ ] User Entity 연관관계 추가
- [ ] Question Entity 연관관계 추가
- [ ] QnA Service deleteQuestion 리팩토링