# JPA

## 1단계 요구사항
* [x] user table mapping
* [x] answer table mapping
* [x] question table mapping
* [x] deleteHistory table mapping
* [x] created_at, updated_at baseEntity로 처리

## 2단계 요구사항
* [ ] 아래 DDL을 참고하여 연관 관계 매핑
* [ ] 깨진 테스트 수정

## DDL
```sql
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
