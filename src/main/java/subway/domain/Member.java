package subway.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;

	@OneToMany // (1)
	@JoinColumn(name = "member_id") // (2)
	private List<Favorite> favorites = new ArrayList<>(); // (3)

	protected Member() {

	}

	public Member(String name) {
		this.name = name;
	}

	public void addFavorite(Favorite favorite) {
		favorites.add(favorite);
	}
}
