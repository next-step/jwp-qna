package subway.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public
class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    public Line() {
    }

    public Line(String name) {
        this.name = name;
    }

    public Line(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    @OneToMany(mappedBy = "line"/*자바 멤버 필드 이름이랑 맞출것*/) // (1)
    private List<Station> stations = new ArrayList<>();

    public List<Station> getStations() {
        return stations;
    }

    public void addStation(Station station) {
        stations.add(station);

        station.setLine(this); //연관관계 설정
    }
}
