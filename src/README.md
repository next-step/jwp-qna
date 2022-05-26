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
3. 다대일 연관관계 매핑
   1. Answer -> Question
   2. Answer -> User
   3. DeleteHistory -> User
   4. Question -> User

###2. 리포지토리 클래스
1. 리포지토리 목록
   1. AnswerRepository
   2. DeleteHistoryRepository
   3. QuestionRepository
   4. UserRepository
2. 리포지토리 요구사항
   1. SpringDataJPA의 JPARepository 상속

###3. 질문 삭제 기능
1. 질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태(deleted - boolean type)로 변경한다.
2. 로그인 사용자와 질문한 사람이 같은 경우 삭제할 수 있다.
3. 답변이 없는 경우 삭제가 가능하다.
4. 질문자와 답변 글의 모든 답변자 같은 경우 삭제가 가능하다.
5. 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경한다.
6. 질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.
7. 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory를 활용해 남긴다.