### 직접 해보는 JPA 1

### 1. 엔티티 맛보기
```
@Entity 
@Table(name = "station") 
public class Station {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @Column(name = "name", nullable = false) 
    private String name;
    
    protected Station() { 
    }
}
```

### 2. 리포지토리 맛보기
```
@DataJpaTest
class StationRepositoryTest {
    @Autowired
    private StationRepository stations;
    
    @Test
    void save() {
        Station expected = new Station("잠실역");
        Station actual = stations.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName())
        );
    }

    @Test
    void findByName() {
        String expected = "잠실역";
        stations.save(new Station(expected));
        String actual = stations.findByName(expected).getName();
        assertThat(actual).isEqualTo(expected);
    }
}
```

### 3. 동일성 보장 맛보기
```
@Test
void identity() {
    Station station1 = stations.save(new Station("잠실역"));
    Station station2 = stations.findById(station1.getId()).get();
    assertThat(station1 == station2).isTrue();
}
```

### 4. 변경 감지 맛보기
```
@Test
void update() {
    Station station1 = stations.save(new Station("잠실역"));
    station1.changeName("몽촌토성역");
    Station station2 = stations.findByName("몽촌토성역");
    assertThat(station2).isNotNull();
}
```
