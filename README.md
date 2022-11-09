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
- QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
- 객체의 참조와 테이블의 외래 키를 매핑해서 객체에서는 참조를 사용하고 테이블에서는 외래 키를 사용할 수 있도록 한다.

* [X] Answer, Question 연관 관계 매핑
* [ ] Answer, User 연관 관계 매핑
* [ ] DeleteHistory, User 연관 관계 매핑
* [ ] Question User 연관 관계 매핑