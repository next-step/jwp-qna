# JPA 실습
### 1단계 - 엔티티 매핑
* 요구사항
  * QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
  * 주어진 DDL을 엔티티, 레포지토리 클래스로 작성
    * answer, delete_history, question, user
      (https://edu.nextstep.camp/s/X02BsEA0/ls/5y729AB4)


* 구현
  * Answer.java
  * DeleteHistory.java
    * ContentType @Enumerated(EnumType.STRING)
  * Question.java
  * User.java
    * user_id unique
  * 추가/수정일 필드 JPA Auditing 사용
  * Entity 중복 컬럼 분리


* 리뷰어 피드백(박이슬 님)
  * 미사용 라이브러리 디펜던시 삭제
  * JPA Auditing 기능 추천
  * Entity 중복 컬럼 분리
  * ~~아이유??!?!?~~
  * JPA Auditing 설정 관련 어노테이션 추가 피드백
  * 미사용 코드 삭제
  * 날짜필드 @LastModifiedBy -> @LastModifiedDate 변경 
  * 요구사항과 동일하도록 DDL, Entity 설계 / 개선  
