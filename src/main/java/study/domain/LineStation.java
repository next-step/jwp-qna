package study.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "line_station")
public class LineStation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "line_id")
	private Line line;

	//1.7.6.1.2. 일대일 연관 관계 - 주 테이블에 외래 키 - 양방향 연관 관계
	@OneToOne(mappedBy = "lineStation")
	//1.7.6.2.2. 일대일 연관 관계 - 대상 테이블에 외래 키 - 양방향 연관 관계
	// @OneToOne
	// @JoinColumn(name = "station_id")
	private Station station;
}
