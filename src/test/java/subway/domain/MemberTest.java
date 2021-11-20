package subway.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MemberTest {

    @Autowired
    private MemberRepository members;

    @Autowired
    private FavoriteRepository favorites;


    @Test
    public void save() {
        Member member = new Member("jason");
        member.addFavorite(favorites.save(new Favorite()));
        Member actual = members.save(member);
        members.flush();
    }
}
