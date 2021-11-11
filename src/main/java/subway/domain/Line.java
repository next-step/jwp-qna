package subway.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "line")
    private List<Station> stations = new ArrayList<>();

    protected Line() {
    }

    protected Line(String name) {
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
