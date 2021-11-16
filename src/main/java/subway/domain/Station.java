package subway.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import qna.domain.BaseEntity;

@Entity
@Table(name = "station")
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @ManyToOne
    private Line line;

//    @OneToOne // (1)
//    @JoinColumn(name = "line_station_id") // (2)
//    private LineStation lineStation; // (3)

    @OneToOne(mappedBy = "station")
    private LineStation lineStation;

    protected Station() {
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

    public void setLine(Line line) {
        if (Objects.nonNull(this.line)) {
            this.line.getStations().remove(this);
        }

        this.line = line;
        line.getStations().add(this);
    }

    public void changeName(String name) {
        this.name = name;
    }
}
