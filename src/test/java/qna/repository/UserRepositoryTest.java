package qna.repository;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit4.SpringRunner;
import qna.domain.Question;
import qna.domain.User;
import qna.domain.UserTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void 유저_저장() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        assertAll(
                () -> assertThat(javajigi.getUserId()).isEqualTo("javajigi"),
                () -> assertThat(javajigi.getEmail()).isEqualTo("javajigi@slipp.net")
        );
    }

    @Test
    void UserId로_유저_찾기() {
        userRepository.save(UserTest.SANJIGI);
        Optional<User> sanjigi = userRepository.findByUserId("sanjigi");

        sanjigi.ifPresent(user -> {
            assertThat(user.getUserId()).isEqualTo("sanjigi");
        });
    }

    @Test
    void 모든_데이터_조회하기() {
        userRepository.save(UserTest.SANJIGI);
        userRepository.save(UserTest.JAVAJIGI);

        List<User> allUser = userRepository.findAll();
        assertThat(allUser.size()).isEqualTo(2);
    }

    @Test
    void 모든_유저_삭제() {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.deleteAll();

        List<User> all = userRepository.findAll();
        assertThat(all.size()).isEqualTo(0);
    }

    @DisplayName("not null인 name 필드가 null일 때 에러 throw 테스트")
    @Test
    void 유저_등록시_name_null_테스트() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            User bestsilver = new User(null, "bestsilver", "password", null, null);
            userRepository.save(bestsilver);
        });
    }
}
