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

    @OneToMany(mappedBy = "line") // (1)
    private List<Station> stations = new ArrayList<>(); // (2)

    public Line(final String name) {
        this.name = name;
    }

    protected Line() {
    }

    public void addStation(Station station) {
        stations.add(station);
        station.setLine(this);
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
}

