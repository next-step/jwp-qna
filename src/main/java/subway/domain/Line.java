package subway.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	// 연관 관계 주인 필드 이름
	// Station에 line이라는 필드가 있고 그곳에 line id가 저장되어 있음을 알 수 있음.
	// mappedBy를 안하면 Mapping Table이 생성됨.
	@OneToMany(mappedBy = "line")
	private List<Station> stations = new ArrayList<Station>();

	protected Line() {}

	public Line(final String name) {
		this.name = name;
	}

	public Long getId() {
		return this.id;
	}
	public String getName() {
		return this.name;
	}

	public List<Station> getStations() {
		return this.stations;
	}

	public void addStation(final Station station) {
		this.stations.add(station);
		// Line이 등록이 되었음을 주인에게 알려줘야 함.
		// 연관관계 편의 메서드
		// 무한루프 주의하기
		station.setLine(this);
	}
}
