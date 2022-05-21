package subway.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "station")
public class Station implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "STATION_ID")
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    protected Station() {

    }

    ;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "LINE_ID")
    private Line line;

    public Station(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return true; // 애를 save했을때 select query가 안날라간다.
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Line getLine() {
        return line;
    }
}
