# JPA Mission

## 기능 요구사항

### Step 1

* [x] 엔티티 매핑
    * [x] answer
    * [x] delete_history
    * [x] question
    * [x] user

### Step 2

* [x] 연관 관계 매핑
    * [x] Answer
    * [x] DeleteHistory
    * [x] Question

``` sql
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
