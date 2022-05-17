package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;

import java.util.Optional;
import javax.persistence.PersistenceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("준영속 상태의 동일성 보장 검증")
    void verifyEntityPrimaryCacheSave() {
        User expected = userRepository.save(JAVAJIGI);
        Optional<User> actual = userRepository.findById(expected.getId());

        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual).contains(expected),
                () -> verifyEqualUserFields(actual.get(), expected)
        );
    }

    @Test
    @DisplayName("영속 상태의 동일성 보장 검증")
    void verifyEntityDatabaseSave() {
        User expected = userRepository.save(JAVAJIGI);
        entityFlushAndClear();
        Optional<User> actual = userRepository.findById(expected.getId());

        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual).contains(expected),
                () -> verifyEqualUserFields(actual.get(), expected)
        );
    }

    @Test
    @DisplayName("저장 및 물리 삭제 후 해당 id로 검색")
    void saveAndPhysicalDeleteThenFindById() {
        User expected = userRepository.save(JAVAJIGI);
        userRepository.delete(expected);
        entityFlushAndClear();
        Optional<User> actual = userRepository.findById(expected.getId());

        assertThat(actual).isNotPresent();
    }

    @Test
    @DisplayName("유니크한 UserId가 겹치면 ConstraintViolationException가 발생")
    void duplicateUserId() {
        User duplicateUserId = new User(3L, "javajigi", "password", "name", "mond@gmail.com");
        userRepository.save(JAVAJIGI);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> userRepository.save(duplicateUserId));

    }

    @Test
    @DisplayName("Name을 null 값으로 설정시 PersistenceException이 발생")
    void setNameNull() {
        User expected = userRepository.save(JAVAJIGI);
        expected.setName(null);

        assertThatExceptionOfType(PersistenceException.class)
                .isThrownBy(this::entityFlushAndClear);
    }

    @Test
    @DisplayName("Password를 null 값으로 설정시 PersistenceException이 발생")
    void setPasswordNull() {
        User expected = userRepository.save(JAVAJIGI);
        expected.setPassword(null);

        assertThatExceptionOfType(PersistenceException.class)
                .isThrownBy(this::entityFlushAndClear);
    }

    @Test
    @DisplayName("UserId를 null 값으로 설정시 PersistenceException이 발생")
    void setUserIdNull() {
        User expected = userRepository.save(JAVAJIGI);
        expected.setUserId(null);

        assertThatExceptionOfType(PersistenceException.class)
                .isThrownBy(this::entityFlushAndClear);
    }

    private void entityFlushAndClear() {
        testEntityManager.flush();
        testEntityManager.clear();
    }

    private void verifyEqualUserFields(User u1, User u2) {
        assertAll(
                () -> assertThat(u1.getId()).isEqualTo(u2.getId()),
                () -> assertThat(u1.getUserId()).isEqualTo(u2.getUserId()),
                () -> assertThat(u1.getName()).isEqualTo(u2.getName()),
                () -> assertThat(u1.getEmail()).isEqualTo(u2.getEmail()),
                () -> assertThat(u1.getPassword()).isEqualTo(u2.getPassword()),
                () -> assertThat(u1.getCreatedAt()).isEqualTo(u2.getCreatedAt())
        );
    }
}
