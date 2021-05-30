package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User savedUser;

    @BeforeEach
    void setUp() {
        User expect = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        savedUser = userRepository.save(expect);
    }

    @DisplayName("저장")
    @Test
    public void save() {
        assertThat(savedUser.getId()).isEqualTo(1L);
        assertThat(savedUser.getUserId()).isEqualTo("javajigi");
        assertThat(savedUser.getPassword()).isEqualTo("password");
        assertThat(savedUser.getName()).isEqualTo("name");
        assertThat(savedUser.getEmail()).isEqualTo("javajigi@slipp.net");
    }

    @DisplayName("userId로 검색")
    @Test
    public void findByUserId() {
        User actual = userRepository.findByUserId("javajigi").get();
        assertThat(actual).isEqualTo(savedUser);
    }

}
