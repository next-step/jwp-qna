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

    @OneToOne
    @JoinColumn(name = "line_station_id")
    private LineStation lineStation;

    protected Station() {
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

    public void changeId(Long id) {
        this.id = id;
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
        line.getStations().add(this);
    }
}
