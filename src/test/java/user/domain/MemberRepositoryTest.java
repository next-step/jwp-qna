package user.domain;

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

    @Test
    void save() {
        Member member = new Member("jason");
        member.addFavorite(favorites.save(new Favorite()));
        members.save(member);
        members.flush();
    }
}