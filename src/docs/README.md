
## 1단계 요구사항
- DDL을 보고 유추하여 엔티티 클래스와 리포지토리 클래스 작성
  - 아래 로그 설정을 통해 과제 DDL과 동일한 동작 쿼리가 호출되는지 확인
    - spring.jpa.properties.hibernate.format_sql=true
    - spring.jpa.show-sql=true
  - [X] answer entity, repository 클래스 작성
  - [X] delete_history entity, repository 클래스 작성
  - [X] question entity, repository 클래스 작성
  - [X] user entity, repository 클래스 작성
    - [X] user entity의 user_id 컬럼 UniqueConstraint 지정
- H2 데이터베이스 사용

## 2단계 요구사항
- [X] 엔티티간 연관관계 매핑
  - [X] Answer - ManyToOne - Question 연관관계 매핑
  - [X] Answer - ManyToOne - User 연관관계 매핑
  - [X] DeleteHistory - ManyToOne - User 연관관계 매핑
  - [X] Question - ManyToOne - User 연관관계 매핑

## 3단계 요구사항
- 기능 요구 사항
  - 기본 요구 사항
    - [ ] 질문 삭제시 데이터를 제거하는 것이 아닌 상태(boolean) 변경
    - [ ] 답변 삭제시 데이터를 제거하는 것이 아닌 상태(boolean) 변경
  - 삭제 가능 조건
    - [ ] 로그인 사용자와 질문한 사람이 같은 경우 삭제 가능
    - [ ] 답변이 없는 경우 삭제 가능
      - [ ] 답변이 있는 경우 답변자가 본인만 있는 경우 삭제 가능
  - 삭제 불가능 조건
    - 질문자와 다른 답변자가 1개 이상 답변시 삭제 불가능
  - 기타 조건
    - [ ] 질문 삭제시 답변 모두 삭제 상태로 변경
    - [ ] 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory를 이용해 저장

- 프로그래밍 요구 사항
  - 위의 질문 삭제 기능 (기능 요구 사항) 을 구현한 코드인 QnaService.deleteQuestion() 리팩토링
    - 기존의 코드는 단위 테스트하기 어려운 코드와 단위 테스트 가능한 코드가 섞여 있다.
    - 위의 기능 구현 코드를 분리해 단위 테스트 가능한 코드의 테스트 코드를 구현
    - 리팩토링 후 기존의 QnaServiceTest 코드가 모두 정상 동작해야 한다.