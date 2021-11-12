package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MemberTest {
    @Autowired
    private MemberRepository members;

    @Autowired
    private FavoriteRepository favorites;

    @DisplayName("member 저장")
    @Test
    void save() {
        final Member expected = new Member("lewis");
        // oneToMany에 joinColumn이 되어 있어서 관계 테이블 생성을 안한다.
        expected.addFavorite(favorites.save(new Favorite()));
    }
}
