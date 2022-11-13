# JPA - 엔티티 매핑, 연관 관계 매핑, 질문 삭제하기 리팩터링

## 기능 요구사항
* QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
* DDL(Data Definition Language)을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성해 본다.
* @DataJpaTest를 사용하여 학습 테스트를 해 본다.

* 질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태(deleted - boolean type)로 변경한다.
* 로그인 사용자와 질문한 사람이 같은 경우 삭제할 수 있다.
* 답변이 없는 경우 삭제가 가능하다.
* 질문자와 답변 글의 모든 답변자 같은 경우 삭제가 가능하다.
* 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경한다.
* 질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.
* 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory를 활용해 남긴다.

## 기능 목록
* 엔티티 클래스 
  * 엔티티 클래스에 따른 매핑 어노테이션 추가
  * DDL 에 따라 데이터 스키마 추가 / 수정
  * 엔티티 목록 
    * answer
    * delete_history
    * question
    * user
* 리포지토리 클래스 
  * DDL 에 따라 추가 / 수정
  * @DataJpaTest를 사용하여 학습 테스트 추가
* 연관 관계 
  * answer -> question
  * answer -> user
  * deleted_history -> user
  * question -> user
