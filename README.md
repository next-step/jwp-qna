# 1단계 - 엔티티 매핑

JPA 엔티티 매핑에 대해 학습하기

## 추가적으로 알아보기

- 즉시로딩(FetchType.EAGER)
    - 즉시로딩은 말그대로 데이터를 조회할때 연관된 데이터까지 한번에 불러옵니다.
    - 참조 객체와 항상 함께 로드되어야 하는 조건을 가진 엔티티에 대해선 LAZY 방식보단 EAGER 방식이 더 좋습니다.
    - 즉시 로딩을 적용하면 예상하지 못한 SQL이 발생합니다.
        - @ManyToOne이 5개 있는데 전부 EAGER로 설정되어 있다고 생각해보면...
        - 조인이 5개 일어난다. 실무에선 테이블이 더 많습니다.
    - 즉시로딩은 JPQL에서 N+1 문제를 일으킵니다.


- 지연로딩(FetchType.LAZY)
    - 지연로딩은 필요한 시점에 연관된 데이터를 불러옵니다.


- 연관관계별 fetch 옵션 기본값
    - @ManyToOne : EAGER
    - @OneToOne : EAGER
    - @ManyToMany : LAZY
    - @OneToMany : Lazy


- Fetch Join
    - 조회의 주체가 되는 Entity 이외에 Fetch Join 이 걸린 연관 Entity 도 함께 SELECT 하여 모두 영속화
    - 실제 질의하는 대상 Entity 와 Fetch join 이 걸려있는 Entity 를 포함한 컬럼 함께 SELECT
    - Fetch Join 이 걸린 Entity 모두 영속화하기 때문에 FetchType 이 Lazy 인 Entity 를 참조하더라도 이미 영속성 컨텍스트에 들어있기 때문에
      따로 쿼리가 실행되지 않은 채로 N+1문제가 해결됨
  > 일반 Join : join 조건을 제외하고 실제 질의하는 대상 Entity에 대한 컬럼만 SELECT


- CascadeType 의 종류
    - CascadeType.RESIST – 엔티티를 생성하고, 연관 엔티티를 추가하였을 때 persist() 를 수행하면 연관 엔티티도 함께 persist()가 수행된다.
      만약 연관 엔티티가 DB에 등록된 키값을 가지고 있다면 detached entity passed to persist Exception이 발생합니다.
    - CascadeType.MERGE – 트랜잭션이 종료되고 detach 상태에서 연관 엔티티를 추가하거나 변경된 이후에 부모 엔티티가 merge()를 수행하게 되면
      변경사항이 적용됨(연관 엔티티의 추가 및 수정 모두 반영됨)
    - CascadeType.REMOVE – 삭제 시 연관된 엔티티도 같이 삭제됨
    - CascadeType.DETACH – 부모 엔티티가 detach()를 수행하게 되면, 연관된 엔티티도 detach() 상태가 되어 변경사항이 반영되지 않음.
    - CascadeType.ALL – 모든 Cascade 적용

### @Mock 테스트란 [모의 객체(Mock Object)]

> 모의 객체(Mock Object)란 주로 객체 지향 프로그래밍으로 개발한 프로그램을 테스트 할 경우 테스트를 수행할 모듈과 연결되는 외부의 다른 서비스나 모듈들을 실제 사용하는 모듈을 사용하지 않고 실제의 모듈을 "흉내"내는 "가짜" 모듈을 작성하여 테스트의 효용성을 높이는데 사용하는 객체이다. 사용자 인터페이스(UI)나 데이터베이스 테스트 등과 같이 자동화된 테스트를 수행하기 어려운 때 널리 사용된다.  
> 출처: [위키피디아](https://ko.wikipedia.org/wiki/%EB%AA%A8%EC%9D%98_%EA%B0%9D%EC%B2%B4)

> Mock 객체는 언제 필요한가?   
> 1.테스트 작성을 위한 환경 구축이 어려운 경우  
> 2.테스트가 특정 경우나 순간에 의존적인 경우   
> 3.테스트 시간이 오래 걸리는 경우   
> 4.개인 PC의 성능이나 서버의 성능문제로 오래 걸릴수 있는 경우 시간을 단축하기 위해 사용한다.  
> 출처: https://www.crocus.co.kr/1555 [Crocus]

- @InjectMocks 는 @Mock 이 붙은 객체를 주입시키는 어노테이션

### @Transactional(propagation = Propagation.REQUIRES_NEW)

> Propagation.REQUIRES_NEW로 설정되었을 때에는 매번 새로운 트랜잭션을 시작한다.
> (새로운 연결을 생성하고 실행한다.) 만약, 호출한 곳에서 이미 트랜잭션이 설정되어 있다면(기존의 연결이 존재한다면) 기존의 트랜잭션은 메써드가 종료할 때까지 잠시 대기 상태로 두고 자신의 트랜잭션을 실행한다. 새로운 트랜잭션 안에서 예외가 발생해도 호출한 곳에는 롤백이 전파되지 않는다. 즉, 2개의 트랜잭션은 완전히 독립적인 별개로 단위로 작동한다.  
> 출처 https://jsonobject.tistory.com/467

## 기능 목록

### 1단계 - 엔티티 매핑

- [X] DDL(Data Definition Language)을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성해 본다
- [X] @DataJpaTest 를 사용하여 학습 테스트를 해 본다.
- [X] Spring Data JPA 사용 시 옵션으로 동작 쿼리를 로그를 확인해본다.
- [X] Answer 엔티티 작업
- [X] DeleteHistory 엔티티 작업
- [X] Question 엔티티 작업
- [X] User 엔티티 작업
- [X] AuditingEntityListener base 엔티티 구현하기
- [X] User.update 정상 테스트
- [X] User.update 예외 테스트 (userID null , password 불일치)
- [X] Answer create 예외 테스트 (작성자 null, 질문 null)
- [X] Answer 테스트
    - toQuestion
    - isOwner_질문_작성자_확인
    - deleted_기본값_false
    - deleted
- [X] DeleteHistory 테스트
    - contentType_ENUM_저장확인
- [X] Question 테스트
    - writerId_작성자_일치
    - addAnswer
    - isOwner

### 2단계 - 연관 관계 매핑

> 1.객체의 참조와 테이블의 외래 키를 매핑해서 객체에서는 참조를 사용하고 테이블에서는 외래 키를 사용할 수 있도록 한다.  
> 2.연관된 객체를 찾을 때 외래 키를 사용해서 조인하면 되지만 객체에는 조인이라는 기능이 없다. 객체는 연관된 객체를 찾을 때 참조를 사용해야 한다.

- User entity
    - [X] @Embedded Email 구현
- [X] Answer 와 Question 연관관계 매핑(answer <-> question)
- [X] Answer 와 User 연관관계 매핑(answer -> User)
- [X] DeleteHistory 연관관계 매핑(DeleteHistory -> user)
- [X] Question 연관관계 매핑(question -> user)
- [X] 연관 관계 테스트
- User Test
    - [X] UserGuest.isGuestUser 테스트
    - [X] users Email 검증 테스트
    - [X] user 중복 생성 유니크 실패 테스트
- Question Test
    - [X] Question 삭제시 연관 Answer 같이 remove (deleted) 수행
- Answer Test
    - [X] 게스트 질문 and 답변 작성 예외 테스트
    - [X] Question Lazy 로딩 테스트
- [X] (Question) Answers @Embedded 로 변경
- [X] Question 삭제시 DeleteHistory 리턴
- [X] Answer 삭제시 DeleteHistory 리턴
- [X] QnaServiceTest(@Mock) 테스트 리팩토링
- [X] Setter 제거하고 의미 있는 이름으로 메소드 만들기

### 3단계 - 질문 삭제하기 리팩터링

> 질문,답변 삭제 리팩토링 정책
> - 로그인 사용자와 질문한거 사람이 같아야 삭제 가능
> - 질문자와 답변글 모두 같은 경우 삭제 가능 (답변이 없는 경우 삭제 가능, 질문자와 답변자가 다르면 삭제 할 수 없음 )
> - 질문 삭제시 답변글도 삭제해야 하며, 삭제 상태(deleted) 변경

- [X] 질문 삭제 테스트
    - 자신의 질문 삭제(답변없는경우) 성공 테스트
    - 자신의 답변만 있는 자신의 질문 삭제 성공 테스트
    - 다른 사람 답변이 있는 자신의 질문 삭제 실패 테스트
    - 다른 사람 질문 삭제 실패 테스트
    - 다른 사람 답변 삭제 실패 테스트
    - 자신의 답변 삭제 성공 테스트
- [ ] 의미가 불명한 변수 의미있는 이름으로 리팩토링
- [ ] 질문,답변 삭제, 삭제히 DeleteHistory 남기기
- [ ] QnaServiceTest 테스트 모두 통과하기
- [ ] DeleteHistories 일급컬렉션으로 분리할지 고민하기
- [ ] Delete 로직에 비지니스 로직이 있는데 별도로 분리할 수 없을지 고민하기
- [ ] stream() 으로 코드 개선할 수 있는 부분 계선하기
- [ ] JPA Hands-On 푸쉬하기
