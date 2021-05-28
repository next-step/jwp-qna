package subway.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "line")
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public Line() { }

    public Line(String name) {
        this.name = name;
    }

    public Line(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @OneToMany(mappedBy = "line") // (1)
    private List<Station> stations = new ArrayList<>(); // (2)

    public List<Station> getStations() {
        return this.stations;
    }

    // 연관관계 편의 메서드
    public void addStation(Station station) {
//        this.stations.add(station);
        station.setLine(this);
    }
}