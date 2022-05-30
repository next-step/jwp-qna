package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    EntityManager entityManager;
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("저장이 잘 되는지 테스트")
    void save() {
        User expected = new User("yulmucha", "1234", "Yul", "yul@google.com");
        User actual = userRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getUsername()).isEqualTo(expected.getUsername()),
                () -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword()),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
        );
    }

    @Test
    @DisplayName("userId로 잘 찾아지는지 테스트")
    void findByUserId() {
        String expected = "yulmucha";
        userRepository.save(new User(expected, "1234", "yul", "yul@google.com"));
        String actual = userRepository.findByUsername(expected).get().getUsername();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("개체를 저장한 후 다시 가져왔을 때 기존의 개체와 동일한지 테스트")
    void identity() {
        User u1 = userRepository.save(new User("yulmucha", "1234", "yul", "yul@google.com"));
        User u2 = userRepository.findById(u1.getId()).get();
        assertThat(u1).isSameAs(u2);
    }

    @Test
    @DisplayName("개체를 저장하고 영속성 컨텍스트를 초기화한 후 개체를 다시 가져왔을 때, 저장했을 당시의 개체와 동등한지 테스트")
    void equality() {
        User u1 = userRepository.save(new User("yulmucha", "1234", "yul", "yul@google.com"));
        entityManager.clear();
        User u2 = userRepository.findById(u1.getId()).get();
        assertThat(u1).isEqualTo(u2);
    }
}
