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

트### 2단계 - 연관 관계 매핑 
* 요구사항 
  * QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
    * 객체의 참조와 테이블의 외래 키를 매핑해서 객체에서는 참조를 사용하고 테이블에서는 외래 키를 사용할 수 있도록 한다.
      아래의 DDL을 보고 유추한다.

  * 아래의 DDL을 보고 유추한다.
    <pre><code> alter table answer
      add constraint fk_answer_to_question
      foreign key (question_id)
      references question
      
      alter table answer
      add constraint fk_answer_writer
      foreign key (writer_id)
      references user
      
      alter table delete_history
      add constraint fk_delete_history_to_user
      foreign key (deleted_by_id)
      references user
      
      alter table question
      add constraint fk_question_writer
      foreign key (writer_id)
      references user</code></pre>

## 3단계 - 질문 삭제하기 리팩터링
* 기능 요구 사항
  * QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.
    * 질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태(deleted - boolean type)로 변경한다.
    * 로그인 사용자와 질문한 사람이 같은 경우 삭제할 수 있다.
    * 답변이 없는 경우 삭제가 가능하다.
    * 질문자와 답변 글의 모든 답변자 같은 경우 삭제가 가능하다.
    * 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경한다.
    * 질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.
    * 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory를 활용해 남긴다.

* 구현목록
  * Answers.java
    * Answer List 일급 클래스
    * @Embeddable 
  * QuestionBody.java
    * title 과 contents 를 묶은 클래스 
    * @Embeddable
