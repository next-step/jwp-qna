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