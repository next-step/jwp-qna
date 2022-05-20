package subway.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "line")
    private final List<Station> stations = new ArrayList<>();

    protected Line() {

    }

    public Line(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Station> getStation() {
        return stations;
    }

    public void addStation(Station station) {
        stations.add(station);
        station.setLine(this);
    }
}
