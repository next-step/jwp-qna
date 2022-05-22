package user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.properties")
class MemberRepositoryTest {
    @Autowired
    MemberRepository members;

    @Autowired
    FavoriteRepository favorites;

    @DisplayName("연관 관계 처리를 위한 UPDATE SQL을 추가로 실행해야 한다")
    @Test
    void save() {
        Member expected = new Member("jason");
        expected.addFavorite(favorites.save(new Favorite()));
        Member actual = members.save(expected);
        members.flush(); // transaction commit
    }
}