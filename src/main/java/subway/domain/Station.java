package subway.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "station")
public class Station {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    @OneToOne(mappedBy = "station")
    private LineStation lineStation;

    protected Station() {
    }

    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Station(String name) {
        this.name = name;
    }

    public Station(String name, LineStation lineStation) {
        this.name = name;
        this.lineStation = lineStation;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public Line getLine() {
        return line;
    }

    public void changeLine(Line line) {
        if (Objects.nonNull(this.line)) {
            this.line.getStations().remove(this);
        }
        this.line = line;
        if (line != null && !line.contains(this)) {
            line.getStations().add(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(id, station.id)
                && Objects.equals(name, station.name)
                && Objects.equals(line, station.line)
                && Objects.equals(lineStation, station.lineStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, line, lineStation);
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", line=" + line +
                ", lineStation=" + lineStation +
                '}';
    }
}
