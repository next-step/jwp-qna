package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import qna.config.JpaAuditingConfig;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.UserTest.JAVAJIGI;

@DataJpaTest
@Import(JpaAuditingConfig.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void userId로_user를_조회한다() {
        // given
        userRepository.save(JAVAJIGI);
        // when
        Optional<User> result = userRepository.findByUserId(JAVAJIGI.getUserId());
        // then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void userId가_null인_user를_저장하면_예외가_발생한다() {
        // given
        User user = new User(null, "password", "name", "email");
        // when
        assertThatThrownBy(() ->
                userRepository.save(user)
        ).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void userId는_중복으로_저장될_수_없다() {
        // given
        User user = new User("unique", "password", "name", "email");
        User duplicateUser = new User("unique", "password", "name", "email");
        // when
        assertThatThrownBy(() ->
                userRepository.saveAll(Arrays.asList(user, duplicateUser))
        ).isInstanceOf(DataIntegrityViolationException.class);
    }
}