package subway.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    //mappedBy는 stations에서 line이라는 이름으로 외래키를 관리하고 있음을 명시
    @OneToMany(mappedBy = "line")
    private List<Station> stations = new ArrayList<>();

    protected Line(){

    }

    public Line(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public List<Station> getStations() {
        return this.stations;
    }

    public void addStation(Station station) {
        this.stations.add(station);
        station.setLine(this);
    }
}
