package subway.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private Line line;

    @OneToOne
    @JoinColumn(name = "line_station_id")
    private LineStation lineStation;

    protected Station() {

    }

    public Station(String name, LineStation lineStation) {
        this.name = name;
        this.lineStation = lineStation;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Station(String name) {
        this.name = name;
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
}
