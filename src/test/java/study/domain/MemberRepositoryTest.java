package study.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository members;

    @Autowired
    FavoriteRepository favorites;

    @Test
    @DisplayName("저장 테스트")
    void save() {
        Member expected = new Member("mond");
        expected.addFavorite(favorites.save(new Favorite()));
        members.save(expected);
        members.flush();

        Member actual = members.findByName("mond");
        assertThat(actual.getName()).isEqualTo("mond");
    }
}
