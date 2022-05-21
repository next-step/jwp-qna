package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import qna.config.JpaAuditingConfig;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
}