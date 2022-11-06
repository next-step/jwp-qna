package learningtest.subway;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Line {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Embedded
	private Stations stations = new Stations();

	protected Line() {
	}

	public Line(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Line(String name) {
		this(null, name);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Stations getStations() {
		return stations;
	}

	public void addStation(Station station) {
		stations.add(station);
		station.setLine(this);
	}
}
