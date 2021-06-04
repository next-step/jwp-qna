package study.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

// 1.3.2. 데이터베이스 스키마 자동 생성 - 실습
@Entity
public class Station {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	//1.7.3. 다대일 단방향 연관 관계
	//1.7.4. 양방향 연관 관계
	@ManyToOne
	@JoinColumn(name = "line_id")
	private Line line;

	//1.7.6.1.1. 일대일 연관 관계 - 주 테이블에 외래 키 - 단방향 연관 관계
	//1.7.6.1.2. 일대일 연관 관계 - 주 테이블에 외래 키 - 양방향 연관 관계
	@OneToOne
	@JoinColumn(name = "line_station_id")
	//1.7.6.2.2. 일대일 연관 관계 - 대상 테이블에 외래 키 - 양방향 연관 관계
	// @OneToOne(mappedBy = "station")
	private LineStation lineStation;

	protected Station() {
	}

	public Station(String name) {
		this.name = name;
	}

	public Station(String name, LineStation lineStation) {
		this.name = name;
		this.lineStation = lineStation;
	}

	public Long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void changeName(String name) {
		this.name = name;
	}

	public void setLine(Line line) {
		this.line = line;
	}

	public Line getLine() {
		return this.line;
	}

	//1.7.4.4. 연관 관계 편의 메서드
	public void setLineInteraction(Line line) {
		this.line = line;
		line.getStations().add(this);
	}
}
