# 🚀 1단계 - 엔티티 매핑

## 📄 요구 사항
- QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
    + 아래의 DDL(Data Definition Language)을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성해 본다.
    + @DataJpaTest를 사용하여 학습 테스트를 해 본다.

## 힌트
- Spring Data JPA 사용 시 아래 옵션은 동작 쿼리를 로그로 확인할 수 있게 해준다.
    + spring.jpa.properties.hibernate.format_sql=true
    + spring.jpa.show-sql=true

## 📈 To-Do
- [x] 공통으로 사용할 엔티티 BaseEntity 작성
- [x] User 엔티티 jpa 적용
- [x] Question 엔티티 jpa 적용
- [x] Answer 엔티티 jpa 적용
- [x] DeleteHistory 엔티티 jpa 적용
- [x] UserRepository 테스트 작성
- [x] QuestionRepository 테스트 작성
- [x] AnswerRepository 테스트 작성
- [x] DeleteHistoryRepository 테스트 작성


# 🚀 2단계 - 연관 관계 매핑

## 📄 요구 사항
- QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
    + 객체의 참조와 테이블의 외래 키를 매핑해서 객체에서는 참조를 사용하고 테이블에서는 외래 키를 사용할 수 있도록 한다.

## 힌트
- 이전 단계에서 엔티티 설계가 이상하다는 생각이 들었다면 객체 지향 설계를 의식하는 개발자고, 그렇지 않고 자연스러웠다면 데이터 중심의 개발자일 것이다. 객체 지향 설계는 각각의 객체가 맡은 역할과 책임이 있고 관련 있는 객체끼리 참조하도록 설계해야 한다.
  ```
  Question question = findQuestionById(questionId);
  List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(questionId);
  ```
- 위 방식은 객체 설계를 테이블 설계에 맞춘 방법이다. 특히 테이블의 외래 키를 객체에 그대로 가져온 부분이 문제다. 왜냐하면 관계형 데이터베이스는 연관된 객체를 찾을 때 외래 키를 사용해서 조인하면 되지만 객체에는 조인이라는 기능이 없다. 객체는 연관된 객체를 찾을 때 참조를 사용해야 한다.
  ```
  Question question = findQuestionById(questionId);
  List<Answer> answers = question.getAnswers();
  ```

## 연관 관계
- 질문
    + 질문은 답변들의 정보를 알고 있다.
    + 질문과 답변은 일대다(1:N) 양방향 관계다.
    + 질문과 사용자는 다대일(N:1) 단방향 관계다.
- 답변
    + 답변은 질문 정보를 알고 있다.
    + 답변과 질문은 다대일(N:1) 양방향 관계다.
    + 답변과 사용자는 다대일(N:1) 단방향 관계다.
- 사용자
    + 사용자와 질문은 일대다(1:N) 단방향 관계다.
    + 사용자와 답변은 일대다(1:N) 단방향 관계다.
    + 사용자와 삭제기록은 일대다(1:N) 단방향 관계다.
- 삭제기록
    + 삭제기록과 사용자는 다대일(N:1) 단방향 관계다.

## 📈 To-Do
- [x] Answer 연관 매핑 적용
- [ ] Question 연관 매핑 적용
- [ ] DeleteHistory 연관 매핑 적용
- [ ] User 연관 매핑 적용
