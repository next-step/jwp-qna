package subway.domain;

import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "line")
@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "line")
    @ReadOnlyProperty // 조회 용도
    private List<Station> stations = new ArrayList<>();

    protected Line() {
    }

    public Line(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // 연관관계 편의 메서드
    public void addStation(Station station) {
        stations.add(station);
//        station.setLine(this); // 무한루프 끊기. 조회용도로만..
    }

    public List<Station> getStations() {
        return stations;
    }
}
