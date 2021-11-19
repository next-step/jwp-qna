package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User( 1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    UserRepository userRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    User user;

    @BeforeEach
    void init() {
        user = TestUserFactory.create();
    }

    @Test
    void 저장() {
        //when
        User savedUser = save(user);

        // then
        assertThat(savedUser.getId()).isNotNull();
    }

    @Test
    void 검색() {
        // given
        User savedUser = save(user);

        //when
        User foundUser = userRepository.findById(savedUser.getId()).get();

        //then
        assertThat(foundUser).isEqualTo(savedUser);
    }

    @Test
    void 삭제() {
        // given
        save(user);

        // when
        userRepository.delete(user);

        // then
        assertThat(userRepository.findById(user.getId()).isPresent()).isFalse();
    }

    private User save(User user) {
        return userRepository.save(user);
    }
}
