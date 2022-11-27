# 2주차 
## 1단계 - 엔티티 매핑

##  요구사항
- 아래의 DDL(Data Definition Language)을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성해 본다.
    - 엔티티 클래스
    - 리포지토리 클래스
- ``@DataJpaTest``를 사용하여 학습 테스트를 진행한다.

* [x] Answer 클래스 작성, AnswerRepository 테스트 작성
* [x] DeleteHistory 클래스 작성, DeleteHistoryRepository 테스트 작성
* [x] Question 클래스 작성, QuestionRepository 테스트 작성
* [x] User 클래스 작성, UserRepository 테스트 작성
## 2단계 - 연관 관계 매핑
#### 기능 요구사항
* [x] Answer - Question 연관 관계 매핑
* [x] Answer - User 연관 관계 매핑
* [x] DeleteHistory - User 연관 관계 매핑
* [x] Question - User 연관 관계 매핑

## 3단계 - 질문 삭제하기 리팩터링
#### 기능 요구사항
* [X] 질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태(deleted - boolean type)로 변경
* [X] 로그인 사용자와 질문한 사람이 같은 경우 삭제 가능
* [X] 답변이 없는 경우 삭제가 가능
* [X] 질문자와 답변 글의 모든 답변자 같은 경우 삭제가 가능
* [X] 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경
* [X] 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory를 활용

#### 프로그래밍 요구사항
* [ ] 단위 테스트 가능한 부분과 어려운 부분을 분리하여 테스트 가능한 부분에 대해 TDD 구현
* [ ] 리팩터링을 완료한 후에도 src/test/java 디렉터리의 qna.service.QnaServiceTest의 모든 테스트가 통과