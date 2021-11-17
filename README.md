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
