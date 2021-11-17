# 요구사항 정리
### 1단계
 - DDL 참고하여 answer entity 생성한다.
    - id는 AutoIncrement 이다.
 - DDL 참고하여 delete_history entity 생성한다.
     - id는 AutoIncrement 이다.
 - DDL 참고하여 question entity 생성한다.
 - DDL 참고하여 user entity 생성한다.
    - user_id는 unique 하다.

### 2단계
- answer
   - question과 N : 1 관계이다.
   - user와 N : 1 관계이다.
- delete_history
   - content(question or answer) 와 1 : 1 관계이다.
- question
   - user와 N : 1 관계이다.