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
- [ ] User 엔티티 jpa 적용
- [ ] Question 엔티티 jpa 적용
- [ ] Answer 엔티티 jpa 적용
- [ ] DeleteHistory 엔티티 jpa 적용
- [ ] UserRepository 테스트 작성
- [ ] QuestionRepository 테스트 작성
- [ ] AnswerRepository 테스트 작성
- [ ] DeleteHistoryRepository 테스트 작성
