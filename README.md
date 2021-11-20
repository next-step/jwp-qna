# 기능목록
## 1단계
* Answer 클래스를 엔티티로 메핑한다.
    * created_at,deleted 칼럼은 null이 허용되지 않는다.
* AnswerRepository의 대한 학습테스트를 한다.
    * save 결과 Answer의 id가 자동으로 생성되어야한다.
    * null을 허용하지 않는 칼럼들에 값이 존재하는지 테스트한다.
    * findById 결과는 동일성이 보장되어야한다.
  
* DeleteHistory 클래스를 엔티티로 메핑한다.
  
* Question 클래스를 엔티티로 메핑한다.
    * createdAt, deleted, title 칼럼은 null을 허용하지 않는다.
* QuestionRepository에 대한 학습테스트를 한다.
    * save 결과 Question의 id가 자동으로 생성되어야한다.
    * null을 허용하지 않는 칼럼들에 값이 존재하는지 테스트한다.
    * findById 결과는 동일성이 보장되어야한다.

* User 클래스를 엔티티로 메핑한다.
    * created_at, name, password, user_id는 null을 허용하지 않는다.
    * userId에 unique 제약사항을 추가한다. 
* UserRepository에 대한 학습테스트를 한다.
    * save 결과 id가 자동으로 생성되어야 한다.
    * null을 허용하지 않는 칼럼들에 값이 존재하는지 테스트한다.
    * findById 결과는 동일성이 보장되어야한다.
    * 이미 존재하는 userId를 save 할때 예외가 발생한다.
    * update를 하면 이름과 메일이 수정될수 있다.

*## 2단계 - 연관 관계 매핑*
* 질문과 사용자 사이의 연관 관계 : 단방향 다대일   
  * 사용자는 여러개의 질문을 남길수 있다.
  * 질문의 하나의 사용자에게만 남겨질수 있다.
* 답변과 질문 사이의 연관 관계 : 단방향 다대일
  * 질문 하나에 대한 여러 답변이 있을수 있다.
* 삭제기록과 사용자 사이의 연관관계 : 단방향 다대일
  * 사용자는 여러개의 삭제기록을 가질수 있다.
* 질문과 답변 사이의 연관관계 : 단방향 다대일
  * 질문하나당 여러개의 답변이 있을수 있다.