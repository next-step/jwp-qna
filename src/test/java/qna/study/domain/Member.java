package qna.study.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String email;

	@ManyToOne
	@JoinColumn(name = "team_id")
	private Team team;

	public Member() {
	}

	public Member(String name, String email) {
		this.name = name;
		this.email = email;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public void addTeam(Team team) {
		this.team = team;
		team.getMembers().add(this);
	}

	public Long getId() {
		return id;
	}
}
