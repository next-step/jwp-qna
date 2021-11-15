package subway.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import subway.domain.Line;

@Entity
@Table(name = "line_station")
public class LineStation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "line_id")
	private Line line;

	public LineStation() {
	}

	public LineStation(Line line) {
		this.line = line;
	}

	public Line getLine() {
		return line;
	}

	public Long getId() {
		return id;
	}
}
