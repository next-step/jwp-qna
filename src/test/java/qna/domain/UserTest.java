package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    UserRepository users;

    @Test
    void save() {
        // when
        User actual = users.save(JAVAJIGI);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getCreatedAt()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(JAVAJIGI.getName())
        );
    }

    @Test
    void identity() {
        // given
        User actual = users.save(JAVAJIGI);

        // when
        User DB조회 = users.findById(actual.getId()).get();

        assertThat(actual).isEqualTo(DB조회);
    }

}
