package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import qna.domain.User;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAllInBatch();
    }

    @Test
    void 유저_저장_테스트() {
        User user = userRepository.save(JAVAJIGI);
        assertAll(
                () -> assertThat(user.getUserId()).isEqualTo(JAVAJIGI.getUserId()),
                () -> assertThat(user.getEmail()).isEqualTo(JAVAJIGI.getEmail())
        );
    }

    @Test
    void 유저_저장_후_유저ID로_조회_테스트() {
        User saveUser = userRepository.save(JAVAJIGI);
        Optional<User> findUser = userRepository.findByUserId(saveUser.getUserId());

        assertThat(findUser.isPresent()).isTrue();
        findUser.ifPresent(user -> assertAll(
                () -> assertThat(user).isEqualTo(saveUser),
                () -> assertThat(user.getUserId()).isEqualTo(saveUser.getUserId())
        ));
    }

    @Test
    void 유저_저장시_동일한_userId를_넣으면_예외_발생_테스트() {
        userRepository.save(SANJIGI);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> userRepository.save(SANJIGI)).withMessageContaining("constraint");
    }

    @Test
    void 유저_저장시_id_값을_null로_보내면_DB가_id값을_넣어줌_테스트() {
        User user = new User("javajigi", "password", "name", "javajigi@slipp.net");
        User saveUser = userRepository.save(user);

        assertThat(saveUser.getId()).isNotNull();
    }

    @Test
    void 유저_조회_후_삭제하면_더_이상_조회가_되지_않음_테스트() {
        User saveUser = userRepository.save(SANJIGI);
        Long saveUserId = saveUser.getId();
        Optional<User> findUser = userRepository.findById(saveUserId);

        assertThat(findUser.isPresent()).isTrue();
        findUser.ifPresent(user -> userRepository.delete(user));
        Optional<User> deletedUser = userRepository.findById(saveUserId);

        assertThat(deletedUser.isPresent()).isFalse();
    }
}
