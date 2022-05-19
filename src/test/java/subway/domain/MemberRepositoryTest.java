package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository members;

    @Autowired
    private FavoriteRepository favorites;

    @Test
    @DisplayName("일대다 단방향 매핑 - 저장 테스트")
    void save() {
        // given
        Member expected = new Member("jason");
        expected.addFavorite(favorites.save(new Favorite()));

        // when
        Member actual = members.save(expected);

        // then
        members.flush(); // transaction commit
    }
}
