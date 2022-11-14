# 2주차 JPA

## 1단계
- QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
- 아래의 DDL(Data Definition Language)을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성해 본다.
@DataJpaTest를 사용하여 학습 테스트를 해 본다.

## 2단계
- QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
  - 객체의 참조와 테이블의 외래 키를 매핑해서 객체에서는 참조를 사용하고 테이블에서는 외래 키를 사용할 수 있도록 한다.
- alter table answer
  add constraint fk_answer_to_question
  foreign key (question_id)
  references question
- alter table answer
add constraint fk_answer_writer
foreign key (writer_id)
references user
- alter table delete_history
add constraint fk_delete_history_to_user
foreign key (deleted_by_id)
references user
- alter table question
add constraint fk_question_writer
foreign key (writer_id)
references user

## 1단계 요구사항 정리
> - [x] 1.공통 Entity 추가
> - [x] 2.answer 맵핑
> - [x] 3.delete_history 맵핑
> - [x] 4.question 맵핑
> - [x] 5.user 맵핑

## 2단계 요구사항 정리
> - [ ] 1.Question <-> Answer 연관관계 맵핑
> - [ ] 2.User <-> Answer 연관관계 맵핑
> - [ ] 3.User <->Delete_history 연관관계 맵핑
> - [ ] 4.User <-> Question 연관관계 맵핑


