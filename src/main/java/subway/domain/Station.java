package subway.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity // (1)
public class Station {
    @Id // (3)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // (4)
    private Long id;

    @Column(nullable = false) // (5)
    private String name;

    @ManyToOne
    private Line line;

    protected Station() { // (6) 기본 생성자가 있어야 함
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

    public void setLine(Line line) {
        this.line = line;
        line.getStations().add(this);
    }

    public Line getLine() {
        return line;
    }
}
