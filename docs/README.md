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

## 3단계
- QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
- 질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태(deleted - boolean type)로 변경한다.
- 로그인 사용자와 질문한 사람이 같은 경우 삭제할 수 있다.
- 답변이 없는 경우 삭제가 가능하다.
- 질문자와 답변 글의 모든 답변자 같은 경우 삭제가 가능하다.
- 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경한다.
- 질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.
- 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory를 활용해 남긴다.

## 1단계 요구사항 정리
> - [x] 1.공통 Entity 추가
> - [x] 2.answer 맵핑
> - [x] 3.delete_history 맵핑
> - [x] 4.question 맵핑
> - [x] 5.user 맵핑

## 2단계 요구사항 정리
> - [x] 1.Question <-> Answer 연관관계 맵핑
> - [x] 2.User <-> Answer 연관관계 맵핑
> - [x] 3.User <->Delete_history 연관관계 맵핑
> - [x] 4.User <-> Question 연관관계 맵핑


## 3단계 요구사항 정리
> - [ ] 1.Question
>   - [ ] 데이터 삭제 상태(deleted - boolean type) 변경.
>   - [ ] 로그인 사용자와 질문자가 다르면 삭제 불가능
>   - [ ] 답변이 있으면 삭제 불가능.
>   - [ ] 질문자와 답변 글의 모든 답변자가 같은 경우 삭제 가능.
>   - [ ] 질문이 삭제 되었을 경우 deleteHistory에 데이터를 남긴다.
>   - [ ] answer의 List를 일급컬렉션으로 정의
> - [ ] 2.Answer
>   - [ ] 데이터 삭제 상태(deleted - boolean type) 변경.
>   - [ ] 로그인 사용자와 질문자가 다르면 삭제 불가능
>   - [ ] 질문자와 답변자가 다른 경우 답변 삭제 불가능
>   - [ ] 질문이 삭제 되었을 경우 deleteHistory에 데이터를 남긴다.
> - [ ] 3.DeleteHistory
>   - [ ] 삭제 데이터 저장.