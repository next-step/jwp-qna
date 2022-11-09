package subway.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    @ManyToOne
    private Line line;

    protected Station() {
    }

    protected Station(final String name) {
        this.name = name;
    }

    protected Station(final Long id, final String name) {
        this.id = id;
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

    public void setLine(final Line line) {
        this.line = line;
        if(!line.getStations().contains(this)){
            line.getStations().add(this);
        }
    }
}
