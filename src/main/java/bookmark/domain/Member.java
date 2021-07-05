package bookmark.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
        favorites.add(favorite);
//        favorite.setMember(this);
    }
}