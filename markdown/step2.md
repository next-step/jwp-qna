# 2단계 - 연관 관계 매핑

## 요구사항

### QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다

- 객체의 참조와 테이블의 외래 키를 매핑해서 객체에서는 참조를 사용하고 테이블에서는 외래 키를 사용할 수 있도록 한다.

```SQL
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

## TODO LIST

### Entity

- [ ] 단순 밸류 값으로 매핑된 것 객체로 변경
  - [ ] Answer에 있는 questionId 변경
  - [ ] Answer에 있는 writerId 변경
  - [ ] DeleteHistory에 있는 deletedById 변경
  - [ ] Question에 있는 writerId 변경
- [ ] 다대일, 일대다, 일대일 설정
- [ ] 연관관계의 주인인 엔티티에 편의메서드 추가
- [ ] 주인이 아닌 엔티티에 ReadOnly 추가
