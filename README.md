##1단계 요구사항
* 테이블 생성을 위한 엔티티 클래스와 리포지토리 클래스 작성, 학습 테스트 작성
    * answer
    * delete_history
    * question
    * user

##2단계 요구사항
* 엔티티 관계 매핑
  * answer : question = 다대일
  * answer : user = 다대일
  * delete_history : user = 다대일
  * question : user = 다대일