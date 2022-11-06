package learningtest.subway;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Stations {

	@OneToMany(mappedBy = "line")
	private List<Station> stations = new ArrayList<>();

	protected Stations() {
	}

	public Stations(List<Station> stations) {
		this.stations = stations;
	}

	public void remove(Station station) {
		this.stations.remove(stations);
	}

	public boolean contains(Station station) {
		return stations.contains(station);
	}

	public void add(Station station) {
		stations.add(station);
	}
}
