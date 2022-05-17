## 요구 사항

QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.

- 객체의 참조와 테이블의 외래 키를 매핑해서 객체에서는 참조를 사용하고 테이블에서는 외래 키를 사용할 수 있도록 한다.

### 힌트

이전 단계에서 엔티티 설계가 이상하다는 생각이 들었다면 객체 지향 설계를 의식하는 개발자고, 그렇지 않고 자연스러웠다면 데이터 중심의 개발자일 것이다. 객체 지향 설계는 각각의 객체가 맡은 역할과 책임이 있고 관련 있는 객체끼리 참조하도록 설계해야 한다.

```java
Question question = findQuestionById(questionId);
List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(questionId);
```

위 방식은 객체 설계를 테이블 설계에 맞춘 방법이다. 특히 테이블의 외래 키를 객체에 그대로 가져온 부분이 문제다. 왜냐하면 관계형 데이터베이스는 연관된 객체를 찾을 때 외래 키를 사용해서 조인하면 되지만 객체에는 조인이라는 기능이 없다. 객체는 연관된 객체를 찾을 때 참조를 사용해야 한다.

```java
Question question = findQuestionById(questionId);
List<Answer> answers = question.getAnswers();
```
아래의 DDL을 보고 유추한다.

- H2

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

- MySQL

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

## 기능 목록

- [x] 이전 개선 사항 적용
- [x] 연관 매핑 적용
