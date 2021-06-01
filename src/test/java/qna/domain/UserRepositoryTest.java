package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserRepositoryTest {
    public static final User JAVAJIGI = new User("javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
    public static final User INSUP = new User("insup", "password", "name", "insup@slipp.net");

    @Autowired
    private UserRepository userRepository;

    private User javajigi;

    @BeforeEach
    void setUp() {
        javajigi = userRepository.save(JAVAJIGI);
    }

    @DisplayName("저장")
    @Test
    void save() {
        //when, then
        assertThat(javajigi).isNotNull();
        assertThat(javajigi.getUserId()).isEqualTo("javajigi");
    }

    @DisplayName("userId로 찾기")
    @Test
    void findByUserId() {
        //when
        User findUser = userRepository.findByUserId("javajigi")
                .orElseThrow(NoSuchElementException::new);

        //then
        assertThat(findUser).isEqualTo(javajigi);
        assertThat(findUser).isSameAs(javajigi);
    }
}
