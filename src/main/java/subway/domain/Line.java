package subway.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    //연관 관계의 주인을 말해주지 않으면 따로 관리 테이블을 만든다 ( default )
    @OneToMany(mappedBy = "line") // foreign key를 관리하는 필드
    private List<Station> stations = new ArrayList<>();

    protected Line() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Line(String name) {
        this.name = name;
    }

    public void addStation(Station station) {
        stations.add(station);
        station.setLine(this);
    }
}
