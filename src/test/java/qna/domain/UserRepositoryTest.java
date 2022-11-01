package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserRepository 테스트")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void user_저장_테스트() {
        User actual = userRepository.save(UserTest.JAVAJIGI);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertEquals("javajigi", actual.getUserId()),
                () -> assertEquals("password", actual.getPassword()),
                () -> assertEquals("name", actual.getName()),
                () -> assertEquals("javajigi@slipp.net", actual.getEmail()),
                () -> assertThat(actual.getCreatedAt()).isNotNull(),
                () -> assertThat(actual.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    void user_동일성_보장_테스트() {
        User userSaved = userRepository.save(UserTest.JAVAJIGI);
        User userFound = userRepository.findById(userSaved.getId()).get();

        assertTrue(userSaved == userFound);
    }

    @Test
    void user_업데이트_테스트() {
        User userSaved = userRepository.save(
                new User("user1", "password", "user1", "user1@test.com")
        );
        userSaved.setName("userName_changed");

        userRepository.saveAndFlush(userSaved);

        assertAll(
                () -> assertEquals("userName_changed", userSaved.getName()),
                () -> assertTrue(userSaved.getUpdatedAt().isAfter(userSaved.getCreatedAt()))
        );
    }

    @Test
    void userId로_user_검색() {
        User userSaved = userRepository.save(UserTest.JAVAJIGI);
        User userFound = userRepository.findByUserId(userSaved.getUserId()).get();

        assertEquals(userSaved, userFound);
    }

}
