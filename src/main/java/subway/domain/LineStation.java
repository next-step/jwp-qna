package subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "line_station")
public class LineStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "station_id")
    private Station station;

    protected LineStation() {
    }

    public LineStation(Line line, Station station) {
        this.line = line;
        this.station = station;
        station.addLineStation(this);
    }

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public Station getStation() {
        return station;
    }

    @Override
    public String toString() {
        return "LineStation{" +
            "id=" + id +
            ", line=" + line.getId() +
            ", station=" + station.getId() +
            '}';
    }
}