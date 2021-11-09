package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    @DisplayName("user 저장 테스트")
    @Test
    void userSaveTest() {
        //given
        LocalDateTime now = LocalDateTime.now();
        User savedUser = userRepository.save(new User("id", "password", "name", "email"));

        //when
        User findByUser = userRepository.findByUserId(savedUser.getUserId())
                .get();

        //then
        assertAll(() -> {
            assertThat(findByUser.getId(), is(notNullValue()));
            assertThat(findByUser.getId(), is(savedUser.getId()));
            assertThat(findByUser.getUserId(), is(savedUser.getUserId()));
            assertThat(findByUser.getPassword(), is(savedUser.getPassword()));
            assertThat(findByUser.getName(), is(savedUser.getName()));
            assertThat(findByUser.getEmail(), is(savedUser.getEmail()));
            assertTrue(findByUser.getCreatedDate().isAfter(now));
            assertTrue(findByUser.getModifiedDate().isAfter(now));
        });
    }
}
