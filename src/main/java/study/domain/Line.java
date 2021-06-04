package study.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "line")
public class Line {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	//1.7.4. 양방향 연관 관계
	@OneToMany(mappedBy = "line")
	private List<Station> stations = new ArrayList<>();

	public Line() {
	}

	public Line(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public List<Station> getStations() {
		return this.stations;
	}

	public void addStation(Station station) {
		this.stations.add(station);
	}

	//1.7.4.4. 연관 관계 편의 메서드
	public void addStationInteraction(Station station) {
		stations.add(station);
		station.setLine(this);
	}
}
