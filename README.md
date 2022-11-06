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
  - [x  ] 매핑 학습 테스트