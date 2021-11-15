package subway.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;

	// mappedBy를 사용하지 않는 경우 JoinColumn을 통해 주인 설정 가능
	@OneToMany
	@JoinColumn(name = "member_id")
	private List<Favorite> favorites = new ArrayList<Favorite>();

	protected Member() {}

	public Member(final String name) {
		this.name = name;
	}

	public void addFavorite(final Favorite favorite) {
		this.favorites.add(favorite);
	}
	
}
