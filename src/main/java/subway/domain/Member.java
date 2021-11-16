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

    @OneToMany // (1) 때로는 정답일때가 있다.
    @JoinColumn(name = "member_id")
    // (2) -> OneToMany 에서는  Favorite 테이블에 member_id 컬럼이 생성된다, favorite 는 연관관계를 알지 못하는 단점이 있다.
    private List<Favorite> favorites = new ArrayList<>(); // (3)

    public Member(String name) {
        this.name = name;
    }

    public void addFavorite(Favorite favorite) {
        favorites.add(favorite);
    }
}
