2단계-연관관계 매핑
===
# 미션 설명
### 요구사항
> QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고, 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
- 객체의 참조와 테이블의 외래 키를 매핑해서 객체에서는 참조를 사용하고 테이블에서는 외래 키를 사용할 수 있도록 한다.

### 힌트
- 이전 단계에서 엔티티 설계가 이상하다는 생각이 들었다면 객체 지향 설계를 의식하는 개발자고, 그렇지 않고 자연스러웠다면 데이터 중심의 개발자일 것이다. 객체 지향 설계는 각각의 객체가 맡은 역할과 책임이 있고 관련 있는 객체끼리 참조하도록 설계해야 한다.
```
Question question = findQuestionById(questionId);
List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(questionId);
```
- 위 방식은 객체 설계를 테이블 설계에 맞춘 방법이다. 특히 테이블의 외래 키를 객체에 그대로 가져온 부분이 문제다. 왜냐하면 관계형 데이터베이스는 연관된 객체를 찾을 때 외래 키를 사용해서 조인하면 되지만 객체에는 조인이라는 기능이 없다. 객체는 연관된 객체를 찾을 때 참조를 사용해야 한다.
```properties
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
```
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

---
# 미션 진행

### 구현 목표
- 주어진 DDL을 통해 외래 키 컬럼을 필드로 매핑한 부분을 객체 관점에서 연관관계 매핑으로 수정

### 구현 기능 목록 : TODO List
- 엔티티 매핑
  - [x] 질문 엔티티 매핑 수정 
    - 질문 엔티티와 질문 작성자의 연관관계 설정
  - [x] 답변 엔티티 매핑 수정
    - 답변과 답변 작성자의 연관관계 설정
    - 질문과 답변의 연관관계 설정
  - [x] 삭제 이력 엔티티 매핑 수정

---
### 구현 시 주안점
DB 컬럼 중심적인 매핑에서 객체 간 연관관계 매핑으로 변경
- 객체 간 다중성에 따른 연관관계 설정
  - `@ManyToOne`을 이용하여 객체간 `1:N` 연관관계 매핑
    - 엔티티 조회 시, 프록시 초기화 과정에서 발생할 수 있는 `N+1` 문제 방지를 위한 LazyLoading 옵션 설정
  - 다중성에 따른 외래 키 관리 주체 설정
    - `@JoinColumn`을 이용하여 외래 키 관련 매핑 정보 설정
  - 연관관계 설정에 따른 TC 및 검증 항목 수정

### 피드백 내용 정리
- JPA Audit
  - JPA Entity에 이벤트가 발생할 때 데이터 변경 추적을 위해 콜백을 실행하여 메타성 데이터를 관리하는 기능
  - Pre*, Post* 시점에 persist, remove, update 등의 Event 처리를 위한 EventListener 제공

- Spring Data JPA Audit
  - Spring Data JPA는 AuditingEntityListener라는 EventListener 구현체를 제공
  - @PrePersist와 @PreUpdate 콜백 메서드를 통해 flush가 발생하는 시점에 AuditingHandler에 의해 @CreatedBy, @CreatedDate, @LastModifiedBy, @LastModifiedDate 값 설정
  - Entity의 isNew 상태에 따라 생성 일시와 수정 일시에 해당하는 값을 판별하며, 값 설정 시 CurrentDateTimeProvider의 기본 구현체를 제공
    - CurrentDateTimeProvider은 LocaDateTime 타입을 다루므로 글로벌 서비스 등을 위해 UTC(ZonedDateTime)를 다뤄야 하는 경우 Listener를 직접 구현하는 경우를 고려
  - EnableJpaAuditing의 modifyOnCreate 설정을 통해 엔티티가 영속 처리되는 시점에 Modified 관련 이벤트를 처리하지 않도록 설정 가능

- JPA Entity의 isNew 판별
   - isNew 판별 기준 : PK에 해당하는 식별자 필드의 값이 null 또는 0일 경우
   - PK에 해당하는 식별자를 DB채번 전략이 아닌 String 등의 값을 할당하는 경우 Entity에 Persistable 인터페이스의 isNew 직접 구현을 통해 판별 기준을 정의

---
