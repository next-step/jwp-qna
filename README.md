# JPA

## step3 - 질문 삭제하기 리팩토링
* 질문 데이터 삭제는 데이터를 지우는게 아닌 상태를 삭제 상태로 변경
* 질문 작성자만 삭제 가능
* 답변이 없는 경우 삭제 가능
* 질문자와 답변 글의 모든 답변자가 같은 경우 삭제 가능
* 질문 삭제시 답변 또한 삭제하며, 답변도 삭제 상태로 변경
* 질문과 답변 삭제 이력에 대한 정보를 저장

## step2 - 연관 관계 매핑
* answer - question 매핑
* answer - user 매핑
* question - user 매핑
* delete_history - user 매핑

## step1 - 엔티티 매핑
* Answer 엔티티 작성
* Answer 테스트
* Question 엔티티 작성
* Question 테스트
* Delete_history 엔티티 작성
* Delete_history 테스트
* User 엔티티 작성
* User 테스트