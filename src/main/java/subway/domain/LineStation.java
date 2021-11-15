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

//	@OneToOne(mappedBy = "lineStation")
//	private Station station;

	@OneToOne
	@JoinColumn(name = "station_id")
	private Station station;

	protected LineStation() {}
}
