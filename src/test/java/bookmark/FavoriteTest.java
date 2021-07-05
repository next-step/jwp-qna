package bookmark;

import bookmark.domain.Favorite;
import bookmark.domain.FavoriteRepository;
import bookmark.domain.Member;
import bookmark.domain.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class FavoriteTest {

    @Autowired
    private FavoriteRepository favorites;

    @Autowired
    private MemberRepository members;

    @Test
    void save() {
        Member expected = new Member("jason");
        expected.addFavorite(favorites.save(new Favorite()));
        Member actual = members.save(expected);
        members.flush();
    }
}
