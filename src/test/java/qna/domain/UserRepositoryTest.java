package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    }

    @DisplayName("저장 후 조회시, 동등성과 동일성을 보장해야 한다")
    @Test
    void find() {
        final User saved = userRepository.save(user);
        final User actual = userRepository.findById(saved.getId()).get();
        assertThat(actual).isEqualTo(saved);
        assertThat(actual).isSameAs(saved);
    }

    @DisplayName("저장 후 갱신시, 영속선 컨텍스트 캐시로 인해 동등성과 동일성을 보장해야 한다")
    @Test
    void update() {
        final User saved = userRepository.save(user);

        // Update name and email
        User updated = userRepository.findById(saved.getId()).get();
        updated.update(updated, new User(saved.getUserId(), saved.getPassword(), "withbeth", "wbg"));

        final User actual = userRepository.findById(saved.getId()).get();
        assertThat(actual.getName()).isEqualTo("withbeth");
        assertThat(actual.getEmail()).isEqualTo("wbg");
    }

    @DisplayName("저장 후 삭제 가능 해야 한다")
    @Test
    void delete() {
        final User saved = userRepository.save(user);

        userRepository.deleteById(saved.getId());
        assertThat(userRepository.findById(saved.getId()).orElse(null)).isNull();
    }

}
