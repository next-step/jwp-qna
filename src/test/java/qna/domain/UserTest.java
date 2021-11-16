package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
    @Autowired
    EntityManager em;
    User user1;
    Question question1;
    Answer answer1;

    @BeforeEach
    void init() {
        user1 = new User("javajigi", "password", "name", "javajigi@slipp.net");
        question1 = new Question("title1", "contents1").writeBy(user1);
        answer1 = new Answer(user1, question1, "Answers Contents1");
        userRepository.save(user1);
    }

    @Test
    void 저장() {
        User foundUser = userRepository.findById(user1.getId()).get();
        assertAll(
                () -> assertThat(user1.getId()).isNotNull(),
                () -> assertThat(user1.getUserId()).isEqualTo(foundUser.getUserId())
        );
    }

    @Test
    void 검색() {
        User foundUser = userRepository.findById(user1.getId()).get();
        assertThat(foundUser).isEqualTo(user1);
    }

    @Test
    void 수정() {
        user1.setEmail("test@gmail.com");
        em.flush();
        em.clear();
        User foundUser = userRepository.findById(user1.getId()).get();
        assertThat(foundUser.getEmail()).isEqualTo("test@gmail.com");
    }

    @Test
    void 삭제() {
        userRepository.delete(user1);
        assertThat(userRepository.findById(user1.getId()).isPresent()).isFalse();
    }
}
