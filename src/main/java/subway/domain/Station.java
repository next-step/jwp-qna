package subway.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Station")
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    protected Station() {

    }

    public Station(final String name) {
        this.name = name;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void setLine(Line line) {
        if(Objects.nonNull(this.line)) {
            this.line.getStations().remove(this);
        }
        this.line = line;
        line.getStations().add(this);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }
}
