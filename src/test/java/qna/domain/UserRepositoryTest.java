package qna.domain;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(User.builder().userId("javajigi").password("password").name("name").email("javajigi@slipp.net").build());
        user2 = userRepository.save(User.builder().userId("sanjigi").password("password").name("name").email("sanjigi@slipp.net").build());
    }

    @Test
    @DisplayName("사용자를 저장한다.")
    void save() {
        //given //when //then
        assertAll(
                () -> assertThat(user1.getId()).isNotNull(),
                () -> assertThat(user1.getUserId()).isEqualTo("javajigi")
        );
    }

    @Test
    @DisplayName("사용자 한 건을 조회한다.")
    void findByUserId() {
        //given //when
        Optional<User> findUser = userRepository.findByUserId(user2.getUserId());

        //then
        AssertionsForClassTypes.assertThat(findUser).hasValueSatisfying(user -> assertAll(
                () -> assertThat(user.getId()).isNotNull(),
                () -> assertThat(user.getUserId()).isEqualTo("sanjigi")
        ));
    }
}
