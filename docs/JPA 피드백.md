### 1. Auditing

애플리케이션의 엔티티의 생성 시간과 마지막 수정 시간을 관리할 필요가 있다면 수동으로 매번 추가하는 대신 Auditing 기능을 이용하여 자동으로 추가해 줄 수 있다.

1. @Configuration 클래스에 @EnableJpaAuditing을 추가한다.
```
@EnableJpaAuditing
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

2. 엔티티에 콜백 리스너를 추가한다.
```
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Line {
```

3. 생성 날짜와 마지막 수정 날짜 프로퍼티에 @CreatedDate와 @LastModifiedDate를 추가한다.
```
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Line {
    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;
    
    ...
}
```

@MappedSuperclass

여전히 아래와 같은 코드 중복이 발생한다. @MappedSuperclass를 사용하여 중복 코드를 분리할 수 있다.

```
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;
    
    ...
}
```

BaseEntity는 직접 생성해서 사용하는 경우가 없으므로 추상 클래스로 만든다.

```
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;
    
    ...

}

public class Line extends BaseEntity {
```

---

### 2. JPA

엔티티 비교
- 엔티티의 equals()와 hashCode()를 구현할 때는 엔티티의 식별자만 있어도 충분하다.

Spring Data JPA
- 직접 쿼리 메서드를 만드는 것이 아닌 가급적 Spring Data JPA가 기본으로 제공하는 쿼리 메서드를 사용한다.
- Spring Data JPA는 메서드 이름을 통해 쿼리를 유추할 수 있다. 예를 들어 findByName()은 select l from line l where l.name = :name와 같은 쿼리를 자동으로 만든다.

Spring Data JPA의 save()
- Spring Data JPA의 save()가 동작하는 방식은 아래와 같다.
```
@Transactional
@Override
public <S extends T> S save(S entity) {
    if (entityInformation.isNew(entity)) {
        em.persist(entity);
        return entity;
    } else {
        return em.merge(entity);
    }
}
```
- 엔티티의 식별자가 null이 아닌 경우 persist()가 아닌 merge()가 동작하며 이때 전달 인자인 entity가 아닌 새로운 인스턴스가 반환된다.

```
@Test
void save() {
    Member member = new Member(1L, "jason"); // (1)

    Station sourceStation = stations.save(new Station("잠실역")); // (2)
    Station destinationStation = stations.save(new Station("몽촌토성역")); // (3)

    Favorite favorite = new Favorite(sourceStation, destinationStation); // (4)
    member.addFavorite(favorite);

    System.out.println("### before merge");
    Member savedMember = members.save(member); // (5)
    System.out.println("### after merge");

    System.out.println("### before transaction commit");
    members.flush(); // transaction commit (6)
    System.out.println("### after transaction commit");
}
```

(1) 식별자가 존재하므로 member는 준영속(detached) 상태라 볼 수 있다.

(2) sourceStation는 영속(managed) 상태다.

(3) destinationStation는 영속(managed) 상태다.

(4) favorite는 비영속(new) 상태다.

(5) save()를 호출하면 내부에서는 실제로 merge()가 동작한다. 따라서 member가 아닌 savedMember가 영속 상태가 되며 savedMember에는 연관 관계에 있는 다른 엔티티에 대한 정보는 가지고 있지 않다. (member와 savedMember는 서로 다른 인스턴스이다.)

따라서 아래와 같은 로그가 기록된다.

```
### before merge

select
    member0_.id as id1_3_0_,
    member0_.name as name2_3_0_ 
from
    member member0_ 
where
    member0_.id=1

insert 
into
    member
    (id, name) 
values
    (null, 'jason')

### after merge

### before transaction commit

insert 
into
    favorite
    (id, destination_station_id, source_station_id) 
values
    (null, null, null)

update
    favorite 
set
    member_id=1
where
    id=1

### after transaction commit
```

해결 방법

아래 세 가지 방법이 가능하다.
- 식별자가 존재하는 엔티티인 경우 먼저 영속(managed) 상태로 만든 후 비즈니스 로직을 수행한다.
- favorite를 영속(managed) 상태로 만든다.
- CascadeType.MERGE를 사용한다.

@ManyToOne vs @OneToOne
- 생성되는 DDL은 똑같을 수 있다.
- 관념적으로 차이가 발생한다.
- [Hibernate ManyToOne vs OneToOne](https://stackoverflow.com/questions/18460469/hibernate-manytoone-vs-onetoone)
```
@ManyToOne
@JoinColumn(name = "station_id")
private Station station;

@OneToOne
@JoinColumn(name = "station_id", unique = true)
private Station station;
```

---

### 3. 테스트 간의 격리
스프링 테스트에서 애플리케이션 컨텍스트는 공유해서 사용한다.

이 때문에 직접 자원 정리를 하더라도 데이터베이스(H2)의 자동 증가 값이 초기화되지 않는 등의 문제가 발생하여 테스트가 실패한다.

@DirtiesContext
- @DirtiesContext를 통해 매번 애플리케이션 컨텍스트를 다시 생성하도록 지시할 수 있다.
- 애플리케이션 컨텍스트가 매번 생성되기 때문에 테스트 속도가 느려지는 단점이 있다.
```
@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
```

@Sql
- 지정한 스크립트를 실행해 주는 애너테이션
- TRUNCATE문을 작성하여 동작하도록 한다.
```
@Sql("/truncate.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
```

- H2의 TRUNCATE문은 외래 키로 참조되지 않는 테이블만 제거할 수 있다.
- SET REFERENTIAL_INTEGRITY로 전체 데이터베이스에 대한 참조 무결성 검사를 비활성화할 수 있다.
```
SET REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE favorite RESTART IDENTITY;
TRUNCATE TABLE member RESTART IDENTITY;
TRUNCATE TABLE line_station RESTART IDENTITY;
TRUNCATE TABLE line RESTART IDENTITY;
TRUNCATE TABLE station RESTART IDENTITY;
SET REFERENTIAL_INTEGRITY TRUE;
```
