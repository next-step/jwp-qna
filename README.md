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
    - Fetch Join 이 걸린 Entity 모두 영속화하기 때문에 FetchType 이 Lazy 인 Entity 를 참조하더라도 이미 영속성 컨텍스트에 들어있기 때문에 따로 쿼리가 실행되지 않은 채로
      N+1문제가 해결됨
  > 일반 Join : join 조건을 제외하고 실제 질의하는 대상 Entity에 대한 컬럼만 SELECT


- CascadeType 의 종류
    - CascadeType.RESIST – 엔티티를 생성하고, 연관 엔티티를 추가하였을 때 persist() 를 수행하면 연관 엔티티도 함께 persist()가 수행된다. 만약 연관 엔티티가 DB에 등록된
      키값을 가지고 있다면 detached entity passed to persist Exception이 발생합니다.
    - CascadeType.MERGE – 트랜잭션이 종료되고 detach 상태에서 연관 엔티티를 추가하거나 변경된 이후에 부모 엔티티가 merge()를 수행하게 되면 변경사항이 적용됨(연관 엔티티의 추가 및
      수정 모두 반영됨)
    - CascadeType.REMOVE – 삭제 시 연관된 엔티티도 같이 삭제됨
    - CascadeType.DETACH – 부모 엔티티가 detach()를 수행하게 되면, 연관된 엔티티도 detach() 상태가 되어 변경사항이 반영되지 않음.
    - CascadeType.ALL – 모든 Cascade 적용

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

객체의 참조와 테이블의 외래 키를 매핑해서 객체에서는 참조를 사용하고 테이블에서는 외래 키를 사용할 수 있도록 한다.

- User entity
    - [X] @Embedded Email 구현
- [X] Answer 와 Question 연관관계 매핑(answer <-> question)
- [X] Answer 와 User 연관관계 매핑(answer -> User)
- [X] DeleteHistory 연관관계 매핑(DeleteHistory -> user)
- [X] Question 연관관계 매핑(question -> user)
- [ ] 연관 관계 테스트
- [X] UserGuest.isGuestUser 테스트
- UserRepository
    - [X] users countByUserId 메소드 테스트
    - [X] user 중복 생성 유니크 실패 테스트
    - [X] findByName 사용자이름 목록 조회 테스트
    - [X] 업데이트 쿼리 테스트
    - [X] 동일성 테스트
- [ ] Answer 와 Question 연관관계 매핑(answer <-> question)
- [ ] Answer 와 User 연관관계 매핑(answer -> User)
- [ ] DeleteHistory 연관관계 매핑(DeleteHistory -> user)
- [ ] Question 연관관계 매핑(question -> user)
- [ ] 연관 관계 테스트


