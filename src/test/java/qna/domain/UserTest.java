package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/truncate.sql")
@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @Test
    void 저장_및_조회() {
        User user1 = userRepository.save(JAVAJIGI);
        User user2 = userRepository.save(SANJIGI);

        User retrievedUser1 = userRepository.findById(user1.getId()).get();
        User retrievedUser2 = userRepository.findById(user2.getId()).get();

        assertThat(retrievedUser1.getId()).isEqualTo(retrievedUser1.getId());
        assertThat(retrievedUser2.getId()).isEqualTo(retrievedUser2.getId());
    }
}
