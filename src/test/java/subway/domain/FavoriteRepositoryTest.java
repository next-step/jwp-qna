package subway.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FavoriteRepositoryTest {
    @Autowired
    private FavoriteRepository favorite;

    @Test
    public void createFavorite() {
        Favorite expected = new Favorite();
    }

}