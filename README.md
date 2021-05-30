# [JPA] 2단계 - 연관 관계 매핑

## 요구 사항
- 객체의 참조와 테이블의 외래 키 매핑 
- 아래의 DDL을 보고 유추하여 진행


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

## 구현 내용

### 1. 테이블 관계 정리
#### 1) Answer : Question
- 다대일 [N:1]

#### 2) Answer : User
- 다대일 [N:1]

#### 3) Question : User
- 다대일 [N:1]

#### 4) Delete_history : User
- 다대일 [N:1]

