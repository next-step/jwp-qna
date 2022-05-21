# JPA
## 기능목록

###1. 엔티티 클래스
1. 엔티티 목록
   1. Answer
   2. DeleteHistory
   3. Question
   4. User
2. 엔티티 요구사항
   1. Id 컬럼, Id 생성전략 필수
   2. 기본생성자 필요
   3. @Table, @Column 등 애노테이션 활용
   4. setter 제공하지 않음

###2. 리포지토리 클래스
1. 리포지토리 목록
   1. AnswerRepository
   2. DeleteHistoryRepository
   3. QuestionRepository
   4. UserRepository
2. 리포지토리 요구사항
   1. SpringDataJPA의 JPARepository 상속