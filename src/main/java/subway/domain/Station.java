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

@Entity
@Table(name = "station")    // 생략 시 엔티티 클래스 이름과 같은 테이블로 매핑
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    /* //주 테이블에 외래 키
    @OneToOne
    @JoinColumn(name = "line_station_id")
    private LineStation lineStation;*/

    // 대상 테이블에 외래 키
    @OneToOne(mappedBy = "station")
    private LineStation lineStation;

    public Station(final String name) {
        this.name = name;
    }

    public Station(final String name, final LineStation lineStation) {
        this.name = name;
        this.lineStation = lineStation;
    }

    protected Station() {
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

    public void setLine(final Line line) {
        //line.addStation(this); //-> 무한루프
        if (Objects.nonNull(this.line)) {
            this.line.getStations().remove(this);
        }
        this.line = line;
        line.getStations().add(this);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LineStation getLineStation() {
        return lineStation;
    }

    public void setLineStation(LineStation lineStation) {
        this.lineStation = lineStation;
    }

    public Station get() {
        return this;
    }

    public void changeName(final String name) {
        this.name = name;
    }
}
