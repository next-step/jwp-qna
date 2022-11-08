package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);
    }

    @Test
    void findById() {
        User user1 = userRepository.findById(1L).get();
        User user2 = userRepository.findById(2L).get();
        assertThat(user1.getUserId()).isEqualTo(JAVAJIGI.getUserId());
        assertThat(user2.getUserId()).isEqualTo(SANJIGI.getUserId());
        assertThat(user1.getPassword()).isEqualTo(JAVAJIGI.getPassword());
        assertThat(user2.getPassword()).isEqualTo(SANJIGI.getPassword());
        assertThat(user1.getName()).isEqualTo(JAVAJIGI.getName());
        assertThat(user2.getName()).isEqualTo(SANJIGI.getName());
        assertThat(user1.getEmail()).isEqualTo(JAVAJIGI.getEmail());
        assertThat(user2.getEmail()).isEqualTo(SANJIGI.getEmail());
    }

    @Test
    void findByUserId() {
        User user1 = userRepository.findByUserId("javajigi").get();
        User user2 = userRepository.findByUserId("sanjigi").get();
        assertThat(user1.getId()).isEqualTo(JAVAJIGI.getId());
        assertThat(user2.getId()).isEqualTo(SANJIGI.getId());
        assertThat(user1.getPassword()).isEqualTo(JAVAJIGI.getPassword());
        assertThat(user2.getPassword()).isEqualTo(SANJIGI.getPassword());
        assertThat(user1.getName()).isEqualTo(JAVAJIGI.getName());
        assertThat(user2.getName()).isEqualTo(SANJIGI.getName());
        assertThat(user1.getEmail()).isEqualTo(JAVAJIGI.getEmail());
        assertThat(user2.getEmail()).isEqualTo(SANJIGI.getEmail());
    }
}
