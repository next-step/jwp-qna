package subway.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.DynamicUpdate;

@Entity // (1)
@DynamicUpdate
public class Station {
    @Id // (3)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // (4)
    private Long id;

    @Column(nullable = false) // (5)
    private String name;


    protected Station() { // (6)
    }

    //다대일 1호선 - 대방,신도림
    @ManyToOne
    @JoinColumn
    private Line line;

    //동기화
    public void setLine(final Line line) {
        if (Objects.nonNull(this.line)) {
            this.line.getStation().remove(this);
        }
        this.line = line;
        line.getStation().add(this);
    }

    public void removeLine() {
        this.line = null;
    }

    public Line getLine() {
        return line;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Station(final String name) {
        this.name = name;
    }

    public void changeName(String name) {
        this.name = name;
    }
}
