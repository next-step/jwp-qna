## JPA

### 1단계 엔티티 매핑
- [x] 아래의 DDL(Data Definition Language)을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성해 본다.
- [x] @DataJpaTest를 사용하여 학습 테스트를 해본다.

### 2단계 연관 관계 매핑
- [x] 객체와 테이블 연관관계 매핑

#### 도메인간 연관관계
* Question (1) - Answer (N) => 양방향, 외래키관리자=Answer
* User (1) - Answer (N) => 단방향. 외래키관리자=Answer
* USer (1) - Question (N) => 단반향, 외래키관리자=Answer
* User (1) - Question (N) => 단방향, 외래키관리자=DeleteHistory
