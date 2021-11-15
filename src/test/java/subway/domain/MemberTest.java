package subway.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class MemberTest {
    @Autowired
    private MemberRepository members;

    @Autowired
    private FavoriteRepository favorites;

    @Test
    void save() {
        final Member expected = new Member("test");
        expected.addFavorite(favorites.save(new Favorite()));
        Member actual = members.save(expected);
        members.flush();
    }
}
