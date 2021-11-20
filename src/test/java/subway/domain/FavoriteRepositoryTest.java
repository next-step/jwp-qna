package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import subway.domain.member.Favorite;
import subway.domain.member.Member;
import subway.domain.member.FavoriteRepository;
import subway.domain.member.MemberRepository;

@DataJpaTest
public class FavoriteRepositoryTest {

    @Autowired
    FavoriteRepository favorites;

    @Autowired
    MemberRepository members;

    @Autowired
    TestEntityManager em;

    private Member MEMBER;
    private Favorite FAVORITE;

    @BeforeEach
    public void setUp() throws Exception {
        MEMBER = new Member("wooobo");
        FAVORITE = new Favorite();

        MEMBER = members.save(MEMBER);
        MEMBER.addFavorite(favorites.save(FAVORITE));
    }

    @Test
    void save() {
        // given
        // when
        // then
        Member actual = members.save(MEMBER);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getFavorites()).hasSize(1)
        );
    }

    @Test
    @DisplayName("favorite 취소")
    void remove() {
        // given
        Member expected = members.findByName(MEMBER.getName());

        // when
        expected.removeFavorite(FAVORITE);

        // then
        assertThat(expected.getFavorites()).hasSize(0);
    }
}
