# QnA
## 요구 사항
* QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
* DDL을 보고 유추하여 엔티티 클래스와 레포지토리 클래스를 작성해 본다.
* @DataJpaTest를 사용하여 학습 테스트를 해 본다.
* 객체의 참조와 테이블의 외래 키를 매핑해서 객체에서는 참조를 사용하고 테이블에서는 외래 키를 사용할 수 있도록 한다.
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
- [X] Answer Entity 연관관계 추가
- [X] DelteHistory Entity 연관관계 추가
- [X] User Entity 연관관계 추가
- [ ] Question Entity 연관관계 추가