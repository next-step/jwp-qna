# JPA - 엔티티 매핑, 연관 관계 매핑

## 기능 요구사항
* QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
* DDL(Data Definition Language)을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성해 본다.
* @DataJpaTest를 사용하여 학습 테스트를 해 본다.

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