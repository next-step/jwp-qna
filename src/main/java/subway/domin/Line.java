package subway.domin;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "line")
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    protected Line() {
    }

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "line") //어떤 키와 연관? 연관관계 테이블을 만듬
    private List<Station> stations = new ArrayList<>();

    public Line(String name) {
        this.name = name;
    }

    public Long getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public void addStation(Station station) {
        stations.add(station);
        station.setLine(this);
    }
}
