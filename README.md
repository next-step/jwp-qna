# qna 서비스 jpa

## 관계 예상 그림

- 2차 예상 (테스트 케이스 만든 후)
  
  ![jwp-qna drawio-v2](https://user-images.githubusercontent.com/17772475/141173769-0c66d797-aba8-45ae-9339-045e410c234a.png)


- 1차 예상 (테스트 케이스 만들기 전)
  
  ![jwp-qna drawio](https://user-images.githubusercontent.com/17772475/141093329-1eb6babd-9951-43f2-ad3c-fdaf11c457a9.png)
  

## 할 일
- [ ] 도메인 중심적 사고, 객체 단위로 생각하는 설계 방식 이해
- [x] 각 도메인 별 Entity 설정 및 data 설정
- [x] DataJpaTest 학습 테스트 사용

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
  

- OneToMany에서 JoinColumn 은 나는 관계테이블을 쓰지 않겠어 라는 뜻
  - JoinColumn은 관계마다 달라지기 때문에 더 연구해야 할 필요가 있음
  

- oneToOne lineStation에서 외래키를 가지면 oneToMany 이 될 때 테이블 구조를 그대로 가져갈 수 있음 **(한번 더 이해 필요)**
  

- OneToOne 단방향에서 뒤의 one은 column 이름의 역할만 한다.
  

- 외래키 관리자가 아닌 쪽에서 add나 set을 하면 외래키의 관리자가 추가를 안하면 null이 들어가 연결이 되지 않는다.
  
### JPA 영속성 컨텍스트
- commit 전에 원래 value로 돌아오면 snapshot과 비교를 통해 update 쿼리가 발생하지 않음(1차 캐시 안에 스냅샷과 비교)
  

- findByName과 같은 메소드를 이용하면 1차 캐시의 key가 Id 기준 이기에 데이터 베이스에 직접 접근, 그리고 없기 때문에 flush를 한다. => update 쿼리 발생
  

- flush 메소드 자체는 실무에서는 강제로 호출하지 않는다.
  

- setLine을 null 로 바꿀 때 foreign key 가 null 상태로 되는 것일 뿐 엔티티가 지워지는 것은 아님
  

- 영속 상태가 아닌 값이 저장이나 수정과 같은 쿼리가 발생한다면 오류가 난다.
  
