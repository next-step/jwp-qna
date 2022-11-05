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

## Step3 - 질문 삭제하기 리팩터링
### 요구사항
- [ ] 질문 데이터의 상태를 삭제 상태로 변경한다.
- [x] 로그인 사용자와 질문한 사람이 같지 않으면 삭제가 불가능하다.
- [x] 질문자와 답변 글의 모든 답변자가 같지 않으면 삭제가 불가능하다.
- [ ] 질문의 삭제 상태가 변경되면 답변도 삭제 상태를 변경해야 한다.
- [ ] 질문과 답변 삭제 이력을 `DeleteHistory`에 남긴다.
