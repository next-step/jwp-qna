package subway.domain;

import javax.persistence.*;

@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    //manyToOne, OneToOne 등 To 뒤에 one이 오는 경우 @joinColumn은 고려하지 않아도 된다.
    @ManyToOne
    private Line line;

    protected Station() {

    }

    public Station(final String name) {
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void changeName(final String name) {
        this.name = name;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Line getLine() {
        return this.line;
    }
}
