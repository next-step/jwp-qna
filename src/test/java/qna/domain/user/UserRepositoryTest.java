package qna.domain.user;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자 한 건을 조회한다.")
    void findByUserId() {
        //given //when
        Optional<User> findUser = userRepository.findByUserId(new UserId("javajigi"));

        //then
        AssertionsForClassTypes.assertThat(findUser).hasValueSatisfying(user -> assertThat(user)
                .extracting("userId", "name", "email")
                .contains(new UserId("javajigi"), new Name("user1"), new Email("javajigi@slipp.net"))
        );
    }
}
