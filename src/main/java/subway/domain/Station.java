package subway.domain;

import javax.persistence.*;

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

    protected Station() {
    }

    public Station(String name) {
        this.name = name;
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
        this.line = line;
        line.getStations().add(this); // line.addStation(this)의 경우 StackOverflowError 발생
    }
}
