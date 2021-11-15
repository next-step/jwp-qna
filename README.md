# JPA QnA

## 1단계 - 엔티티 매핑 요구사항 정리
- [X] DDL(Data Definition Language)을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성
- [X] JPA Auditing

## 2단계 - 연관 관계 매핑 요구사항 정리
- [X] Answer -> Question
- [X] Answer -> User
- [X] DeleteHistory -> User
- [X] Question -> User

## 3단계 - 질문 삭제하기 리팩터링 요구사항 정리
- [X] 답변 미존재 시
  - [X] 로그인한 사용자와 질문한 사람이 같은 경우 삭제할 수 있다.
  - [X] 질문의 삭제 상태(deleted)를 true로 변경한다.
- [X] 답변 존재 시
  - [X] 질문자와 답변 글의 모든 답변자가 같은 경우 삭제가 가능하다.
  - [X] 질문과 답변의 삭제 상태(deleted)를 true로 변경한다.
- [X] 삭제 이력을 DeleteHistory에 적재한다.
    
