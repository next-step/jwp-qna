## Step1 - 엔티티 매핑

#### 요구사항 1
 - 엔티티 클래스와 리포지토리 클래스를 작성해 본다.
#### 요구사항 2
 - @DataJpaTest를 사용하여 학습 테스트를 해 본다.
#### 구현 리스트
- [x] user
  - [x] Entity
  - [x] Repository
  - [x] 학습 테스트
  
- [x] answer
  - [x] Entity
  - [x] Repository
  - [x] 학습 테스트
        
- [x] question
  - [x] Entity
  - [x] Repository
  - [x] 학습 테스트
  
- [x] deleteHistory
  - [x] Entity
  - [x] Repository
  - [x] 학습 테스트
  
#### Step1 리뷰 사항 반영
- [x] Step1 1차 리뷰 사항 반영
    - [x] 불필요한 주석 지우기.
    - [x] Entity, Repository 패키지 분리하기.
    
## Step2 - 엔티티 매핑

#### 요구사항 1
 - 객체의 참조와 테이블의 외래 키를 매핑해서 객체에서는 참조를 사용하고 테이블에서는 외래 키를 사용할 수 있도록 한다.
#### 구현 리스트
- [x] question
  - [x] user 연관관계 매핑 ( writer_id -> user( id ) )
  - [x] 매핑 학습 테스트
  
- [x] deleteHistory
  - [x] user 연관관계 매핑( deleted_by_id -> user( id ) )
  - [x] 매핑 학습 테스트
  
- [x] answer
  - [x] question 연간관계 매핑 ( question_id -> question( id ) )
  - [x] user 연간관계 매핑 ( writer_id -> user( id ) )  
  - [x] 매핑 학습 테스트
  
#### Step2 리뷰 사항 반영
- [x] Step2 1차 리뷰 사항 반영
    - [x] 생명주기를 고려해서 CascadeType.PERSIST 수정
    - [x] 환경을 고려해서 fetch 전략 변경하기
    - [x] toString 각 entity정보는 자기가 표출하도록 변경
    
## Step3 - 질문 삭제하기 리팩토링

#### 요구사항 1
 - 질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태(deleted - boolean type)로 변경한다.
 - 로그인 사용자와 질문한 사람이 같은 경우 삭제할 수 있다.
 - 답변이 없는 경우 삭제가 가능하다.
 - 질문자와 답변 글의 모든 답변자 같은 경우 삭제가 가능하다.
 - 질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경한다.
 - 질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.
 - 질문과 답변 삭제 이력에 대한 정보를 DeleteHistory를 활용해 남긴다.
#### 구현 리스트
- [x] 테스트 리팩토링 ( EntityTest 사용 )
    - [x] user
    - [x] question
    - [x] answer
    - [x] deleteHistory
- [x] Question Delete기능 추가
    - [x] 로그인 사용자와 질문한 사람이 같은 경우만 삭제 가능
    - [x] 답변이 없는 경우 삭제 가능
    - [x] 실제 삭제가 아닌 삭제 상태 변경
- [x] Answer Delete기능 추가 ( 답변이 존재 )
    - [x] 질문자와 답변자가 같은 경우만 삭제 가능 ( 즉 로그인 사용자 = 질문자 = 모든 답변자여야 삭제 가능 )
    - [x] 질문자와 답변자가 다를 경우, 삭제가 불가능하다
    - [x] 실제 삭제가 아닌 삭제 상태 변경
- [x] deleteHistory에 이력 남기는 기능 추가
    - [x] 질문만 존재하는 경우 - 질문 삭제 후 이력을 남긴다
    - [x] 질문과 답변이 동시에 존재하는 경우 - 질문뿐만 아니라 답변에 대해서도 이력을 남긴다