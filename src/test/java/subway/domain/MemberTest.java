package subway.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberTest {
    @Autowired
    private MemberRepository members;

    @Autowired
    private FavoriteRepository favorites;

    @Test
    void save() {
        final Member expected = new Member("이름");
        expected.addFavorite(favorites.save(new Favorite()));
        members.save(expected);
        favorites.flush();
    }

}