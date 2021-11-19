# 요구사항 정리
### 1단계 (엔티티 매핑)
 - DDL 참고하여 answer entity 생성한다.
    - id는 AutoIncrement 이다.
 - DDL 참고하여 delete_history entity 생성한다.
     - id는 AutoIncrement 이다.
 - DDL 참고하여 question entity 생성한다.
 - DDL 참고하여 user entity 생성한다.
    - user_id는 unique 하다.

### 2단계 (연관 관계 매핑)
- answer
   - question과 N : 1 관계이다.
   - user와 N : 1 관계이다.
- delete_history
   - content(question or answer) 와 1 : 1 관계이다.
- question
   - user와 N : 1 관계이다.
    
### 3단계 (질문 삭제하기 리팩터링)
- 삭제 요청시 deleted - boolean type true 값으로 수정
- 로그인 사용자와 질문한 사람이 같은 경우 삭제할 수 있다.
- 답변이 없는 경우 삭제가 가능하다.
- 질문자와 답변 글의 모든 답변자 같은 경우 삭제가 가능하다.
- 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경한다.
- 질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.
- 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory를 활용해 남긴다.