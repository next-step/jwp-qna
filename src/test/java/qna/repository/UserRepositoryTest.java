package qna.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;
import qna.domain.UserRepository;
import qna.domain.UserTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void save() {
        User expected = new User("tester", "Tester1!", "테스터", "tester@test.com");
        User actual = userRepository.save(expected);
        assertAll(
                () -> assertThat(expected.getUserId()).isEqualTo(actual.getUserId()),
                () -> assertThat(expected.equalsNameAndEmail(actual)).isEqualTo(true)
        );
    }

    @Test
    void findAll() {
        User expected = new User("tester", "Tester1!", "테스터", "tester@test.com");
        userRepository.save(expected);
        List<User> actual = userRepository.findAll();
        assertThat(actual.get(0).getUserId()).isNotNull();
        assertThat(actual.get(0).getCreatedAt()).isNotNull();
        System.out.println(actual.get(0).toString());
    }

    @Test
    void findByUserId() {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);
        User actual = userRepository.findByUserId(UserTest.JAVAJIGI.getUserId()).get();
        assertThat(actual.getUserId()).isEqualTo(UserTest.JAVAJIGI.getUserId());
    }
}