# qna 서비스 jpa

## 관계 예상 그림

- 4차 예상 (step2 - 1차 피드백 후)

  ![jwp-qna drawio-v4](https://user-images.githubusercontent.com/17772475/141789834-f8434d22-fc8c-4855-96c0-677e705d5fb6.png)


- 3차 예상 (연관 관계 매핑 후)

  ![jwp-qna drawio-v3](https://user-images.githubusercontent.com/17772475/141682152-7a9aac6f-3410-4ac2-9771-90cafdb32169.png)
  

## 할 일
- [ ] 도메인 중심적 사고, 객체 단위로 생각하는 설계 방식 이해
- [x] 각 도메인 별 Entity 설정 및 data 설정
- [x] DataJpaTest 학습 테스트 사용
  
## 현재 고민 중인 부분
- Question과 Answer를 하나로 묶을 수 있는 개념이 필요한지에 대한 고민
  - DeleteHistory에서 contentId로만 Question과 Answer를 받고 있는데 이 부분을 조정하기 위해서는
  - Question과 Answer의 부모격인 엔티티나 객체가 나와서 id들을 받아주는 것이 좋겠다고 생각함
  - 현재 생각나는 네이밍은 Content이다. (근데 Question과 Answer만을 포함하는 추상적인 개념이 아니라 조심스러움)
  
- 유저가 Question, Answer, DeleteHistory를 몰라도 될까? 에 대한 고민

- Referential integrity constraint violation [FK 참조 무결성 위반](https://devwithpug.github.io/spring/jpa-1/#:~:text=%EC%9D%B4%20%EC%97%90%EB%9F%AC%EB%8A%94%20%EC%B0%B8%EC%A1%B0%20%EB%AC%B4%EA%B2%B0%EC%84%B1,%ED%95%B4%EC%84%9C%20%EB%B0%9C%EC%83%9D%ED%95%98%EB%8A%94%20%EB%AC%B8%EC%A0%9C%EC%9D%B4%EB%8B%A4.)
- 원인 : Question 에 등록된 User 엔티티가 등록되지 않고 객체로만 있었어서 flush()할 때 에러가 발생
- 하지만 왜 참조 무결성 위반 에러가 떴는지는 연구가 필요

## 학습 테스트 하며 기록한 글

### JPA 연관 관계
- 객체 연관관계
  - station을 통해 line 접근 가능 station -> line  
  - line을 통해 station 접근 불가능 line -> station  
  - 연관관계 주인 : station   
  - line쪽에서도 station에 접근이 가능, station 쪽에서도 line에 접근이 가능 
  - 양방향 관계라고 말할 수 있다.
  

- oneToMany 의 many 쪽은 db에 접근 해보기 전까지 내가 외래키 관리자 인지 모르게 때문에 update 쿼리가 일어난다.
  

- 다대일 관계에서 관계 테이블을 만들 필요가 없을 때 oneToMany 쪽에서 mappedBy를 통해 관계 테이블을 생성하지 않을 수 있다.
  - mappedBy 생성하지 않는다면 관계 테이블이 생김 (line_stations)
  

- 외래키를 다루는 쪽이 연관 관계의 주인 (=외래키 관리자)
  - 외래키 다루는 쪽에서는 외래키 등록 조회 수정 삭제와 같은 일들이 다 가능하다 (외래키를 다루지 않는 쪽이라면 키 읽기만 가능)
  

- 단방향 연관관계 일 경우 연관관계의 주인을 설정하는 것이 중요
  

- OneToMany 단방향에서 JoinColumn 은 나는 관계테이블을 쓰지 않겠어 라는 뜻
  - JoinColumn은 관계마다 달라지기 때문에 더 연구해야 할 필요가 있음
  

- oneToOne 단방향에서 lineStation(중간 매개자 같은 느낌으로 생각 중)에서 외래키를 가지면 oneToMany 이 될 때 테이블 구조를 그대로 가져갈 수 있음 **(한번 더 이해 필요)**
  

- OneToOne 단방향에서 뒤의 one은 column 이름의 역할만 한다.


- 외래키 관리자가 아닌 쪽에서 add나 set을 하면 외래키의 관리자가 추가를 안하면 null이 들어가 연결이 되지 않는다.

- [연관 관계 정리](https://jeong-pro.tistory.com/231)
  
### JPA 영속성 컨텍스트
- commit 전에 원래 value로 돌아오면 snapshot과 비교를 통해 update 쿼리가 발생하지 않음(1차 캐시 안에 스냅샷과 비교)
  

- findByName과 같은 메소드를 이용하면 1차 캐시의 key가 Id 기준 이기에 데이터 베이스에 직접 접근, 그리고 없기 때문에 flush를 한다. => update 쿼리 발생
  

- flush 메소드 자체는 실무에서는 강제로 호출하지 않는다.
  

- setLine을 null 로 바꿀 때 foreign key 가 null 상태로 되는 것일 뿐 엔티티가 지워지는 것은 아님
  

- 영속 상태가 아닌 값이 저장이나 수정과 같은 쿼리가 발생한다면 오류가 난다.
  
### JPA Entity toString 순환 참조 현상

원인
- a <-> b 양방향 연관 관계일 때, a.toString 안에 b가 있고 b.toString 안에 a가 있을 경우 서로 계속 부르면서 overflow
- **근본적으로 model에서 view를 부르는 것에 대해서 생각해 볼 필요가 있다.**

해결방법
1. DTO로 만든 후 toString [DTO로 만들기](https://friends-aihaja.tistory.com/entry/5-%EC%96%91%EB%B0%A9%ED%96%A5-%EC%97%B0%EA%B4%80%EA%B4%80%EA%B3%84%EC%99%80-%EC%97%B0%EA%B4%80%EA%B4%80%EA%B3%84-%EC%A3%BC%EC%9D%B8)
2. ToStringBuilder 패키지 이용하기 [ToStringBuilder 패키지 이용하기](https://yellowh.tistory.com/135)

### CascadeType.ALL -> orphanRemoval true 했는데도 불구하고 repository 에서 delete 시도 시 쿼리 발생 안하는 이유

- 연관 관계 매핑이 되어 있는 상황에서 find를 하기 때문에 find 시 다시 persist가 된다. 이는 dirty checking, lazy에 의해 delete 를 발생시키지 않아도 되게한다.
 [원인 설명](https://stackoverflow.com/questions/63030917/spring-jpa-repository-delete-method-doesnt-work)
  
### final 이 없는 이유
- JPA는 엔티티를 생성할 때 지연로딩 방식을 사용
- 이때 프록시 객체(PersistentBag)를 생성하여 사용하기 때문에 필드 또는 클래스에 final을 사용할 수 없다.