
## 🚀 요구사항
* QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.

* 아래의 DDL(Data Definition Language)을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성해 본다.
* @DataJpaTest를 사용하여 학습 테스트를 해 본다.

## 기능 정리
* createdAt과 updatedAt을 관리하는 baseEntity 생성
* 엔티티 매핑
    * Answer 엔티티 매핑
    * DeleteHistory 엔티티 매핑
    * Question 엔티티 매핑
    * User 엔티티 매핑
* Answer test
    * 작성자가 null일 경우 예외가 발생한다
    * 질문이 null일 경우 예외가 발생한다
    * 내가 작성한 댓글인지 확인한다
    * 질문을 설정한다
* Question test
    * 작성자를 설정한다
    * 내가 작성한 질문인지 확인한다
    * 답변을 추가한다
* User test
    * 이름과 이메일을 변경한다
    * 이름과 이메일이 같은지 확인한다
* AnswerRepostiry test
    * id를 넘겨주어 deleted가 false인 질문을 조회한다
    * 변경 감지
* QuestionRepository test
    * id로 조회
    * 저장
    * 삭제
    * 수정
    * id로 삭제되지 않은 질문 조회
    * 삭제되지 않은 질문들을 조회한다
* UserRepository test
    * userId로 user를 조회한다
    * userId가 null인 user를 저장하면 예외가 발생한다
    * userId는 중복으로 저장될 수 없다.
    * password 길이가 20이 넘으면 예외가 발생한다
* DeleteHistory test
    * id로 조회한다
* BaseEntityTest
    * 엔티티가 생성되면 생성일이 저장된다.
    * 엔티티가 수정되면 수정일이 갱신된다. 
  
## 엔티티 매핑 기능 정리
* 연관관계 매핑
  * question
    * user(writer)와 ManyToOne 단방향 매핑
    * answer와 양방향 매핑
  * deleteHistory
    * user(deleter)와 ManyToOne 단방향 매핑
  * answer
    * user(writer)와 ManyToOne 단방향 매핑
    * question과 ManyToOne 양방향 매핑
    * 연관 관계 편의 메서드 정의
* QuestionRepository test
  * 질문에 달린 답변들을 조회한다
  * n + 1 문제 해결

    