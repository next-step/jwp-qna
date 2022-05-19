package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("저장한다")
    void save() {
        User javaUser = userRepository.save(JAVAJIGI);
        User sanUser = userRepository.save(SANJIGI);

        final List<User> users = userRepository.findAll();

        assertAll(()-> {
           assertThat(users).hasSize(2);
           assertThat(users).contains(javaUser, sanUser);
        });
    }

    @Test
    @DisplayName("이름으로 검색한다.")
    void findName() {
        User javaUser = userRepository.save(JAVAJIGI);
        User sanUser = userRepository.save(SANJIGI);

        User findUser = userRepository.findByName(JAVAJIGI.getName());

        assertThat(javaUser).isEqualTo(findUser);
    }

    @Test
    @DisplayName("유저 ID로 검색한다.")
    void findByUserId() {
        User javaUser = userRepository.save(JAVAJIGI);
        User sanUser = userRepository.save(SANJIGI);

        final Optional<User> findUser = userRepository.findByUserId(JAVAJIGI.getUserId());

        assertAll(()-> {
            assertThat(findUser.isPresent()).isTrue();
            assertThat(findUser.get()).isEqualTo(javaUser);
        });
    }


    @Test
    @DisplayName("이름을 변경 한다.")
    void update() {
        User javaUser = userRepository.save(JAVAJIGI);
        javaUser.setName("자바왕");

        User findUser = userRepository.findByName("자바왕");

        assertThat(findUser).isEqualTo(javaUser);
    }

    @Test
    @DisplayName("지운다")
    void delete() {
        User javaUser = userRepository.save(JAVAJIGI);
        User sanUser = userRepository.save(SANJIGI);

        userRepository.delete(javaUser);

        assertThat(userRepository.count()).isEqualTo(1);
    }
}