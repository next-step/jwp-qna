package learningtest.subway;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Station {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "line_id")
	private Line line;

	protected Station() {
	}

	public Station(Long id, String name, Line line) {
		this.id = id;
		this.name = name;
		setLine(line);
	}

	public void setLine(Line line) {
		if (Objects.nonNull(this.line)) {
			this.line.getStations().remove(this);
		}
		this.line = line;
		if (!line.getStations().contains(this)) {
			line.getStations().add(this);
		}
	}

	public Line getLine() {
		return line;
	}
}
