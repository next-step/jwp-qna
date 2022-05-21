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

    @OneToMany(mappedBy = "line") //mappedBy없으면 관계 테이블 별도로 생성 (fk를 관리하는 field)
    private List<Station> stations = new ArrayList<>();

    public List<Station> getStations() {
        return stations;
    }

    protected Line() {
    }

    public Line(final String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addStation(final Station station) {
        stations.add(station);
        //station.getLine()도 null이기 떄문에 직접 넣어줘야한다.
        station.setLine(this);
    }
}
