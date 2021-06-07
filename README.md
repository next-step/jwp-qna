# JPA
## 1단계 - 엔티티 매핑

### 요구 사항
* QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
  * 아래의 DDL(Data Definition Language)을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성해 본다.
  * @DataJpaTest를 사용하여 학습 테스트를 해 본다.  


### 작업 순서
* README 작성
* DDL에 맞게 해당 Entity class 작성
* Entity, Repository Test


## 2단계 - 연관 관계 매핑 

### 요구사항
* QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
  * 객체의 참조와 테이블의 외래 키를 매핑해서 객체에서는 참조를 사용하고 테이블에서는 외래 키를 사용할 수 있도록 한다.
  
### 작업순서
* Step1 리뷰 반영 (JPA Auditing 적용, 접근 제어자 public -> protected 변경)
  * JPA Auditing 적용한 BaseEntity 생성하여 중복 필드 관리
  * 접근 제어자 적절하게 변경
  * 텍스트 정렬
* 연관관계 매핑
