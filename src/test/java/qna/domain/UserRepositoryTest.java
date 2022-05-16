package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import qna.config.QnaDataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.FixtureUser.JAVAJIGI;

@QnaDataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("save하면 id 자동 생성")
    @Test
    void saveTest() {
        final User user = userRepository.save(JAVAJIGI);
        assertThat(user.getId()).isNotNull();
        assertThat(user.getName()).isEqualTo(JAVAJIGI.getName());
    }

    @DisplayName("저장한 엔티티와 동일한 id로 조회한 엔티티는 동일성 보장")
    @Test
    void sameEntity() {
        final User saved = userRepository.save(JAVAJIGI);
        final User user = userRepository.findById(saved.getId()).get();
        assertThat(user).isEqualTo(saved);
    }

    @DisplayName("동일 userId(유니크) 저장시 예외 발생")
    @Test
    void userId() {
        final User user = userRepository.save(JAVAJIGI);
        assertThatThrownBy(() -> {
            final User heowc = new User(user.getUserId(), "1234", "heowc", "heowc@gmail.com");
            userRepository.save(heowc);
        })
        .isInstanceOf(DataIntegrityViolationException.class)
        .hasMessageContaining("could not execute statement");
    }
}
