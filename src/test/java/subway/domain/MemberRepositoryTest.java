package subway.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository members;

    @Autowired
    private FavoriteRepository favorites;

    @Test
    void save() {
        Member expected = new Member("test");
        expected.addFavorite(favorites.save(new Favorite()));
        Member actual = members.save(expected);
        members.flush(); // transaction commit
    }

}
