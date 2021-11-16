# JPA

- QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.

# 1단계 - 엔티티 매핑

### 요구사항

- [X] 아래의 DDL(Data Definition Language)을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성해 본다.
    - [X] Entity 클래스 작성
        - [X] `Answer`
        - [X] `DeleteHistory`
        - [X] `Question`
        - [X] `User`
    - [X] repository 클래스 작성
        - [X] `Answer`
        - [X] `DeleteHistory`
        - [X] `Question`
        - [X] `User`
- [X] @DataJpaTest를 사용하여 학습 테스트를 해 본다.
    - [X] `Answer`
    - [X] `DeleteHistory`
    - [X] `Question`
    - [X] `User`

# 2단계 - 연관 관계 매핑

- QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.

### 요구사항

- [X] 연관 관계 매핑
    - [X] answer
    - [X] delete_history
    - [X] question

``` h2
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
```

# 3단계 - 질문 삭제하기 리팩터링

- 질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태(deleted - boolean type)로 변경한다.
- 로그인 사용자와 질문한 사람이 같은 경우 삭제할 수 있다.
- 답변이 없는 경우 삭제가 가능하다.
- 질문자와 답변 글의 모든 답변자 같은 경우 삭제가 가능하다.
- 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경한다.
- 질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.
- 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory를 활용해 남긴다.

### 요구사항
- [ ] 질문 데이터를 삭제한다. (데이터의 상태를 변경)
  - [X] 질문한 사람이 로그인 사용자인 경우만 삭제할 수 있다.
  - [X] 질문을 삭제할 때 답변도 삭제한다.
    - [X] 질문에 답변이 없는 경우 삭제가 가능하다.
    - [X] 질문자와 답변 글의 모든 답변자가 같은 경우 삭제가 가능하다.
    - [X] 질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.
  - [ ] 삭제 시 이력을 DeleteHistory 에 저장한다.
  
