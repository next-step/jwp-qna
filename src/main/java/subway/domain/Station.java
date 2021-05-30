package subway.domain;

import jdk.nashorn.internal.objects.annotations.Getter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "station")
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

    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
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

    public Line getLine() {
        return line;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void setLine(Line line) {
        if (Objects.nonNull(this.line)) {
            this.line.getStations().remove(this);
        }
        this.line = line;
    }
}
