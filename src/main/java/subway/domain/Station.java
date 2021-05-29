package subway.domain;

import javax.persistence.*;
import java.util.Objects;

@Table(name = "station")
@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne // (1)
    @JoinColumn(name = "line_id") // (2)
    private Line line; // (3)

//    @OneToOne // (1)
//    @JoinColumn(name = "line_station_id") // (2)
//    private LineStation lineStation; // (3)

    public void setLine(Line line) {
        if (Objects.nonNull(this.line)) {
            this.line.getStations().remove(this);
        }
        this.line = line;
        //line.getStations().add(this);
    }

    protected Station() {
    }

    public Station(final String name) {
        this.name = name;
    }


    public Station(final String name, LineStation lineStation) {
        this.name = name;
        //this.lineStation = lineStation;

    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public Line getLine() {
        return this.line;
    }
}
