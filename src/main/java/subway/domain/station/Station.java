package subway.domain.station;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import subway.domain.line.Line;
import subway.domain.line.LineStation;

@Entity
@Table(name = "station")
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    @OneToOne(mappedBy = "station")
    private LineStation lineStation;

    protected Station() {
    }

    public Station(String name, Line line) {
        line.addStation(this);
        this.name = name;
        this.line = line;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Line getLine() {
        return line;
    }

    public void removeLine() {
        this.line = null;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void addLineStation(LineStation lineStation) {
        this.lineStation = lineStation;
    }

    @Override
    public String toString() {
        return "Station{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", line=" + line.getId() +
            ", lineStation=" + lineStation.getId() +
            '}';
    }
}
