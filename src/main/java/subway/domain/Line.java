package subway.domain;

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

	@OneToMany(mappedBy = "line")
	private List<Station> stations = new ArrayList<>();

	protected Line() {}

	public Line(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void addStation(Station station) {
		// 연관관계의 주인이 아닌곳에서 연관관계를 조작하는건 지양하자
		// Station에서 add하는걸 권장!
		stations.add(station);
		station.setLine(this);
	}
}
