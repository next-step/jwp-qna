package subway.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "line_station")
public class LineStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    @OneToOne
    @JoinColumn(name = "station_id")
    private Station station;

    protected LineStation() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineStation that = (LineStation) o;
        return Objects.equals(id, that.id) && Objects.equals(line, that.line) && Objects.equals(station, that.station);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, line, station);
    }

    @Override
    public String toString() {
        return "LineStation{" +
                "id=" + id +
                ", line=" + line +
                ", station=" + station +
                '}';
    }
}
