package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import qna.config.JpaAuditingConfiguration;
import qna.domain.Email;
import qna.domain.Name;
import qna.domain.User;
import qna.domain.UserId;

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
    @DisplayName("패스워드 null 값으로 유저 저장시 예외를 던진다")
    public void save() {
        assertThatThrownBy(() -> userRepository.save(
                new User(1L, new UserId("javajigi"), null, new Name("name"), new Email("test@test.test"))))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("유저 id 가 중복될 경우 예외를 던진다")
    public void uniqueTest() {
        userRepository.save(createUser("DEVELOPYO"));
        assertThatThrownBy(() -> userRepository.save(createUser("DEVELOPYO"))).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("존재하지 않는 데이터 조회시 빈값을 리턴한다")
    public void findByUserIdTestNotExists() {
        User user = createUser("DEVELOPYO");
        Optional<User> byUserId = userRepository.findByUserId(user.getUserId());
        assertThat(byUserId).isEmpty();
    }

    @Test
    @DisplayName("존재하는 유저 조회시 유저를 리턴한다")
    public void findByUserIdTestExists() {
        User user = createUser("DEVELOPYO");
        userRepository.save(user);
        Optional<User> byUserId = userRepository.findByUserId(user.getUserId());
        assertThat(byUserId).isPresent();
    }
}
