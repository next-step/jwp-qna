package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import qna.config.JpaAuditingConfiguration;
import qna.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.DomainTestFactory.createUser;

@DataJpaTest
@Import(value = {JpaAuditingConfiguration.class})
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("null 값 저장시 예외 테스트")
    public void save() {
        assertThatThrownBy(() -> userRepository.save(new User(1L, "javajigi", null, "name", "test@test.test")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("존재하지 않는 데이터 조회시 빈값 리턴 테스트")
    public void findByUserIdTestNotExists() {
        User user = createUser("DEVELOPYO");
        Optional<User> byUserId = userRepository.findByUserId(user.getUserId());
        assertThat(byUserId).isEmpty();
    }

    @Test
    @DisplayName("존재하는 데이터 조회 테스트")
    public void findByUserIdTestExists() {
        User user = createUser("DEVELOPYO");
        userRepository.save(user);
        Optional<User> byUserId = userRepository.findByUserId(user.getUserId());
        assertThat(byUserId).isPresent();
    }
}
