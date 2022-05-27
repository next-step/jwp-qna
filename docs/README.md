## JPA

### 1단계 엔티티 매핑
- [x] 아래의 DDL(Data Definition Language)을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성해 본다.
- [x] @DataJpaTest를 사용하여 학습 테스트를 해본다.

---

### 2단계 연관 관계 매핑
- [x] 객체와 테이블 연관관계 매핑

#### 도메인간 연관관계
* Question (1) - Answer (N) => 양방향, 외래키관리자=Answer
* User (1) - Answer (N) => 단방향. 외래키관리자=Answer
* USer (1) - Question (N) => 단반향, 외래키관리자=Answer
* User (1) - Question (N) => 단방향, 외래키관리자=DeleteHistory

---

## 3단계 질문 삭제하기 리팩터링
### QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
- [x] 질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태(deleted - boolean type)로 변경한다.
- [x] 로그인 사용자와 질문한 사람이 같은 경우 삭제할 수 있다.
- [x] 답변이 없는 경우 삭제가 가능하다.
- [x] 질문자와 답변 글의 모든 답변자 같은 경우 삭제가 가능하다.
- [x] 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경한다.
- [x] 질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.
- [x] 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory를 활용해 남긴다.

### Question Delete 기능 단위테스트 (<U>단위테스트 가능은 밑줄</U>)
- Question 조회</U>
- <U>로그인 유저 <-> Question 작성 유저 검증</U>
- <U>Question의 Answer 목록 조회</U>
- <U>로그인 유저 <-> Answer 작성 유저 검증</U>
- <U>Question 상태변경 (delete)</U>
- <U>Answer 상태변경 (delete)</U>
- DeleteHistory 저장
