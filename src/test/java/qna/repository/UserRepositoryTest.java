package qna.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;
import qna.domain.UserRepository;
import qna.domain.UserTest;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("User 데이터 save 하는 테스트 진행")
    @Test
    public void save() {

        // given
        User user1 = UserTest.JAVAJIGI;
        User user2 = UserTest.SANJIGI;

        // when
        User saved1 = userRepository.save(user1);
        User saved2 = userRepository.save(user2);

        // then
        Assertions.assertThat(saved1).isEqualTo(user1);
        Assertions.assertThat(saved2).isEqualTo(user2);
    }

    @DisplayName("User 데이터 find 하는 테스트 진행")
    @Test
    public void find() {

        // given
        User user = UserTest.HONGHEE;

        // when
        User saved = userRepository.save(user);
        User find = userRepository.findByUserId(saved.getUserId()).get();

        // then
        Assertions.assertThat(find).isEqualTo(saved);
    }
}
