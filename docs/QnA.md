# 요구사항 정리
## 1단계 
  - 주어진 DDL에 맞게 엔티티 클래스 작성
  - 주어진 DDL에 맞게 리포지토리 클래스 작성
  - DataJpaTest로 테스트를 작성한다.

## 2단계 
  - 객체지향적인 설계로 엔티티를 리팩토링한다.

## 3단계 
  - 목표 : service 패키지의 응용 서비스계층에 섞여있는 도메인 로직들을 도메인영역으로 이동시킨다.
  - 기대효과 : 응용서비스 계층과 영속 계층이 구분되어 핵심 도메인로직의 단위테스트 작성이 용이해 진다. 

# 연관관계(연관관계 매핑 전략)
  - Answer : Question = N : 1 (양방향, 외래키관리자 = Answer)
  - Answer : User = N : 1 (단방향, 외래키관리자 = Answer)
  - Question : User = N : 1 (단방향, 외래키관리자 = Question)
  - DeleteHistory : Answer = 1 : 1 (필요시 추후 매핑)
  - DeleteHistory : Question = 1 : 1 (필요시 추후 매핑)
  - DeleteHistory : User = N : 1 (단방향, 외래키관리자 = DeleteHistory)

# 도메인 분석
## 루트 엔티티
  - Question

## 질문 삭제
  - 삭제 가능 조건
    - 로그인 사용자와 질문한 사람이 동일한 경우 삭제 가능
    - 로그인 사용자와 질문한 사람이 동일하지 않은 경우 삭제 불가
    - 답변이 없는 경우 삭제 가능
    - 질문자와 모든 답변 글의 답변자가 동일한 경우 삭제 가능
    - 질문자와 답변자가 다른 경우 삭제 불가
    
  - 삭제 방식
    - 질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태(deleted - boolean type)로 변경한다.
    - 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경한다.
    - 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory를 활용해 남긴다.


# 작업로그
 - [X] Answer에서 시작하는 연관관계 매핑
 - [X] 테스트 코드 리팩토링
   - [X] 테스트코드에만 사용되던 생성자 제거 (identity 키생성 전략에선 필요없음)
     - [X] User
     - [X] Answer
     - [X] Question
   - [X] 테스트 클래스의 테스트 픽스처 상수 제거 ( 테스트 메서드에서 작업한 내역들이 해당 픽스처에 남아 있는 문제 )
     - [X] AnswerTest
     - [X] QuestionTest
     - [X] UserTest
   - [X] QnaServiceTest의 Mock Repository 제거 후 JpaRepository로 변경
 - [X] Question에서 시작하는 연관관계 매핑
   - 연관관계 편의메서드 작성
 - [X] DeleteHistory에서 시작하는 연관관계 매핑
 - [X] 도메인 엔티티 및 로직 분석
 - [X] 도메인 엔티티만 테스트 하기위해 클래스 분리
 - [X] 질문 삭제 기능 리팩토링 
   - [X] 로그인 사용자와 질문한 사람이 동일한 경우 삭제 가능
   - [X] 로그인 사용자와 질문한 사람이 동일하지 않은 경우 삭제 불가
   - [X] 답변이 없는 경우 삭제 가능
   - [X] 질문자와 모든 답변 글의 답변자가 동일한 경우 삭제 가능
   - [X] 질문자와 답변자가 다른 경우 삭제 불가

# 리뷰 내용 반영
## 1단계 
  - [X] BaseEntity에서 id제외하기
  - [X] Exception 패키지 생성 
  - [X] Test 클래스에 @DirtiesContext 제거
  - [X] 테스트 실행시 @EnableJpaAuditing 적용되도록 변경

## 3단계 
  - [X] id를 받는 기본 생성자 삭제(Question, Answer)
    - 테스트 코드에서만 Reflection API로 id부여 
  - [X] DeleteHistoryFactory 작성
  - [X] QuestionDeleteService 작성 및 테스트 리팩토링