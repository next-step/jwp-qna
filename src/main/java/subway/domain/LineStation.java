package subway.domain;

import javax.persistence.*;

@Entity
@Table(name = "line_station")
public class LineStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

//    @OneToOne(mappedBy = "lineStation") // (1)
//    private Station station; // (2)

    @OneToOne // (1)
    @JoinColumn(name = "station_id") // (2)
    private Station station; // (3)

    protected LineStation() {}
}
