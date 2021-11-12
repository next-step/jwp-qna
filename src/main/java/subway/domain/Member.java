package subway.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany
    @JoinColumn(name = "member_id")
    private List<Favorite> favorites = new ArrayList<>();

    protected Member() {

    }

    public Member(String name) {
        this.name = name;
    }

    public void addFavorite(Favorite favorite) {
        favorites.add(favorite);
    }
}
