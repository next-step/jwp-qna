# jwp-qna
QnA 서비스

# STEP 1
## 기능 목록
1. answer 테이블 DDL 생성하도록 answer 엔티티 수정 
2. answer 엔티티에 대한 DML 테스트
3. question 테이블 DDL 생성하도록 question 엔티티 수정
4. question 엔티티에 대한 DML 테스트
5. user 테이블을 DDL 생성하도록 user 엔티티 수정
6. user 엔티티에 대한 DML 테스트


# STEP 2
## 기능 목록
1. answer 테이블에 foreign key로 question id가 존재한다. (Question 테이블)
2. answer 테이블에 foreign key로 writer_id가 존재한다. (User 테이블)
3. delete_history 테이블에 foreign key로 deleted_by_id가 존재한다. (User 테이블)
4. question 테이블에 foreign key로 wirter_id가 존재한다. (User 테이블)


# STEP 3
## 기능 목록
1. 로그인 사용자와 질문을 생성한 사용자가 같은지 확인한다.
 - 1.1. 같지 않다면 예외를 던진다.
2. 질문에 답변이 존재하는지 확인한다.
3. 질문에 답변이 존재한다면 질문에 대한 모든 답변에 답변자와 질문자가 같은지 확인한다.
 - 3.1. 같지 않다면 예외를 던진다.
4. 같다면 질문과 답변을 삭제상태로 변경한다.
5. 질문과 답변에 대한 삭제 이력정보를 추가한다.
