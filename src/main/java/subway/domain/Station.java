package subway.domain;

import javax.persistence.*;

@Entity
@Table(name = "station")
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne // (1)
    @JoinColumn(name = "line_id") // (2)
    private Line line; // (3)

    public void setLine(final Line line) { // (4)
        this.line = line;
    }

    public Station() { }

    public Station(String name) {
        this(null, name);
    }

    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
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
