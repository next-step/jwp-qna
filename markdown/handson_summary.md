# JPA Hands-on 정리

# 1. 개요

## 1.1. 학습 로드맵

- [토크ON 41차. JPA 프로그래밍 기본기 다지기 | T아카데미](https://www.youtube.com/playlist?list=PL9mhQYIlKEhfpMVndI23RwWTL9-VL-B7U)
- [자바 ORM 표준 JPA 프로그래밍 (김영한 저 | 에이콘출판사)](http://www.yes24.com/Product/Goods/19040233)
- [인프런 - 김영한](https://www.inflearn.com/courses?s=%EA%B9%80%EC%98%81%ED%95%9C)
- [영속성 컨텍스트로 보는 JPA](https://www.slideshare.net/ssusere4d67c/jpa-56081624)

## 1.2. 객체 지향 패러다임

- 시스템을 구성하는 객체들에게 적절한 책임을 할당하는 것
- 상속
- 연관 관계
   - 객체의 연관관계 : 방향성이 있다.
   - 테이블의 연관관계 : 방향성이 없다.
- 객체는 자유롭게 객체 그래프를 탐색할 수 있어야 한다.

## 1.3. SQL을 직접 다룰 때 발생하는 문제점

- 반복 작업
   - 새로운 필드가 추가되면 관련된 SQL을 다 수정해야 한다. -> 변경 시 수정 난이도 급증
- 신뢰성
   - SQL을 조회하고도 `Map<String, String>` 으로 변환하여 활용한다?

# 2. JPA

## 2.1. 데이터베이스 스키마 자동 생성

- 애플리케이션 실행 시점에 데이터베이스 테이블을 자동으로 생성한다.
  - `spring.jpa.hibernate.ddl-auto = create`
    - create: 기존 테이블 삭제 후 다시 생성 (DROP + CREATE)
    - create-drop: create와 같으나 종료시점에 테이블 DROP
    - update: 변경된 부분만 반영 (운영 DB에 사용하면 안됌)
    - validate: entity와 table이 정상 매핑되었는지만 확인
    - none: 사용하지 않음

## 2.2. Spring Data JPA

- 메서드 이름으로 쿼리 생성 가능하다.
   - [Appendix C: Repository query keywords](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repository-query-keywords)

## 2.3. 영속성 컨텍스트

- 엔티티를 영구 저장하는 환경
- 엔티티 매니저로 엔티티를 저장하거나 조회하면 엔티티 매니저는 영속성 컨텍스트에 엔티티를 보관하고 관리한다.

> - 1차 캐시 : 데이터베이스의 정보를 관리하기 위해 영속성 컨텍스트에 저장하고 관리한다.
> - 동일성 보장 : 같은 row의 정보를 조회하더라도 동일한 객체가 아닐 수 있다. JPA는 객체의 hash값이 동일하도록 리턴한다. (1차 캐시를 통해 가능하다.) 
> - 트랜잭션을 지원하는 쓰기 지연 : 영속 또는 삭제 상태가 되기 전까지 영속성 컨텍스트에서 변경된 정보는 데이터베이스에 반영되지 않는다.
> - 변경 감지 : 동일성이 보장된 정보를 변경이 된 것을 감지한다.
> - 지연 로딩 : 연관 관계가 맺어진 정보를 지연하여 로딩이 가능하다.

## 2.4. 엔티티의 생명주기

- 비영속(new/transient): 영속성 컨텍스트와 전혀 관계가 없는 상태
- 영속(managed): 영속성 컨텍스트에 저장된 상태
- 준영속(detached): 영속성 컨텍스트에 저장되었다가 분리된 상태
- 삭제(removed): 삭제된 상태

![엔티티의생명주기](../documents/handson/images/LifecycleOfEntity.png)

# 3. 연관 관계

- 엔티티들은 대부분 다른 엔티티와 연관 관계가 있다.
- 객체는 참조(주소)를 사용해서 관계를 맺고 테이블은 외래 키를 사용해서 관계를 맺는다.

## 3.1. 핵심 키워드

- 방향: 단방향, 양방향이 있다.방향은 객체 관계에만 존재하고 테이블 관계는 항상 양방향이다.
- 다중성: 다대일(N:1), 일대다(1:N), 일대일(1:1), 다대다(N:M)
- 연관 관계의 주인: 객체를 양방향 연관 관계로 만들면 연관 관계의 주인을 정해야 한다.
