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

    @OneToMany
    @JoinColumn(name = "member_id")
    private List<Favorite> favorites = new ArrayList<>();

    public Member() {
    }

    public Member(String name) {
        this.name = name;
    }

    public void addFavorite(Favorite favorite) {
        this.favorites.add(favorite);
    }
}
