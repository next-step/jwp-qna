package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;

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
    User savedUser;

    @BeforeEach
    void init() {
        savedUser = userRepository.save(JAVAJIGI);
    }

    @Test
    void 저장() {
        assertAll(
                () -> assertThat(savedUser.getId()).isNotNull(),
                () -> assertThat(savedUser.getUserId()).isEqualTo(JAVAJIGI.getUserId())
        );
    }

    @Test
    void 검색() {
        User user = userRepository.findById(savedUser.getId()).get();
        assertThat(user).isEqualTo(JAVAJIGI);
    }

    @Test
    void 연관관계_답변() {
        questionRepository.save(QuestionTest.Q1);
        Answer savedAnswer = answerRepository.save(AnswerTest.A1);
        savedUser.addAnswer(savedAnswer);
        em.flush();
        em.clear();
        User foundUser = userRepository.findById(savedUser.getId()).get();
        assertThat(foundUser.getAnswers().get(0).getId()).isEqualTo(savedAnswer.getId());
    }

    @Test
    void 연관관계_질문() {
        Question savedQuestion = questionRepository.save(QuestionTest.Q1);
        savedUser.addAQuestion(savedQuestion);
        em.flush();
        em.clear();
        User foundUser = userRepository.findById(savedUser.getId()).get();
        assertThat(foundUser.getQuestions().get(0).getId()).isEqualTo(savedQuestion.getId());
    }

    @Test
    void 연관관계_삭제히스토리() {
        Question savedQuestion = questionRepository.save(QuestionTest.Q1);
        DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(new DeleteHistory(ContentType.QUESTION, savedQuestion.getId(), savedUser, LocalDateTime.now()));
        savedUser.addDeleteHistory(savedDeleteHistory);
        em.flush();
        em.clear();
        User foundUser = userRepository.findById(savedUser.getId()).get();
        assertThat(foundUser.getDeleteHistories().get(0).getId()).isEqualTo(savedDeleteHistory.getId());
    }

    @Test
    void 수정() {
        savedUser.setEmail("test@gmail.com");
        em.flush();
        em.clear();
        User foundUser = userRepository.findById(savedUser.getId()).get();
        assertThat(foundUser.getEmail()).isEqualTo("test@gmail.com");
    }

    @Test
    void 삭제() {
        userRepository.delete(savedUser);
        assertThat(userRepository.findById(savedUser.getId()).isPresent()).isFalse();
    }
}
