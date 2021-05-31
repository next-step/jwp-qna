package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import qna.domain.utils.JpaTest;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserRepository")
class UserRepositoryTest {
    private static final String USER_ID = "user1";
    private static final String PASSWORD = "password";
    private static final String NAME = "test";
    private static final String EMAIL = "test@test.com";
    private User user = new User(USER_ID, PASSWORD, NAME, EMAIL);

    @Nested
    @DisplayName("save 메서드는")
    class Describe_save {

        @Nested
        @DisplayName("사용자 정보가 주어지면")
        class Context_with__data extends JpaTest {
            final User expected = user;

            @Test
            @DisplayName("사용자 정보를 저장하고, 사용자 객체를 리턴한다")
            void it_saves_user_and_returns_user() {
                User actual = getUserRepository().save(expected);

                assertThat(actual).isEqualTo(expected);
            }
        }
    }

    @Nested
    @DisplayName("findByUserId 메서드는")
    class Describe_find_by_user_id {

        @Nested
        @DisplayName("저장된 사용자 정보와 사용자 식별키가 주어지면")
        class Context_with_ extends JpaTest {
            User givenUser;
            final UserId userKey = new UserId(USER_ID);

            @BeforeEach
            void setUp() {
                givenUser = getUserRepository().save(user);
            }

            @Test
            @DisplayName("사용자 식별키에 해당하는 사용자 정보를 리턴한다")
            void it_returns_user() {
                User actual = getUserRepository().findByUserId(userKey)
                        .orElseThrow(EntityNotFoundException::new);

                assertThat(actual).isEqualTo(user);
            }
        }
    }
}
