package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository members;

    @Autowired
    FavoriteRepository favorites;

    @Test
    void save() {
        // given
        Member expected = new Member("jason");

        // when
        expected.addFavorite(favorites.save(new Favorite()));
        Member actual = members.save(expected);
        members.flush();

        // then
        assertThat(actual.getId()).isNotNull();
    }
}
