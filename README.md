# JPA
## Step1 - 엔티티 매핑
### 요구사항
- [x] Answer 도메인 객체에 JPA 엔티티 매핑
- [x] DeleteHistory 도메인 객체에 JPA 엔티티 매핑
- [x] Question 도메인 객체에 JPA 엔티티 매핑
- [x] User 도메인 객체에 JPA 엔티티 매핑
- [x] AnswerRepository 테스트 코드 작성
- [x] DeleteHistoryRepository 테스트 코드 작성
- [x] QuestionRepository 테스트 코드 작성
- [x] UserRepository 테스트 코드 작성

## Step2 - 연관 관계 매핑
### 요구사항
- [x] DDL을 참고하여 연관 관계 매핑
```sql
alter table answer
    add constraint fk_answer_to_question
        foreign key (question_id)
            references question (id)

alter table answer
    add constraint fk_answer_writer
        foreign key (writer_id)
            references user (id)

alter table delete_history
    add constraint fk_delete_history_to_user
        foreign key (deleted_by_id)
            references user (id)

alter table question
    add constraint fk_question_writer
        foreign key (writer_id)
            references user (id)
```