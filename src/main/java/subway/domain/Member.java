package subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Where;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany
    @JoinColumn(name = "member_id", updatable = false)
    @Where(clause = "deleted = false")
    private final List<Favorite> favorites = new ArrayList<>();

    protected Member() {
    }

    public Member(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addFavorite(Favorite favorite) {
        this.favorites.remove(favorite);
        this.favorites.add(favorite);
    }

    public List<Favorite> getFavorites() {
        return Collections.unmodifiableList(favorites);
    }

    public void removeFavorite(Favorite removeFavorite) {
        this.favorites.remove(removeFavorite);
        removeFavorite.delete();
    }
}
