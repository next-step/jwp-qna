package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;
import qna.domain.UserTest;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자ID로 조회하기")
    void findByUserId() {
        //Given
        List<User> users = new ArrayList<>();
        users.add(UserTest.JAVAJIGI);
        users.add(UserTest.SANJIGI);
        users.add(UserTest.HOONHEE);
        userRepository.saveAll(users);

        //When
        User expected = UserTest.HOONHEE;
        User actual = userRepository.findByUserId(UserTest.HOONHEE.getUserId()).get();

        //Then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getUserId()).isEqualTo(expected.getUserId())
        );
    }

}
