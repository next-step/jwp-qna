package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class AnswersTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Answer answer;
    private Question question;
    private Answers answers;

    @BeforeEach
    void setup() {
        user = userRepository.save(UserTest.LEWISSEO);
        answers = new Answers();
        question = questionRepository.save(new Question("title1", "contents1").writeBy(user));
        answer = answerRepository.save(new Answer(user, question, "Answers Contents1"));
        answers.addAnswer(answer);
    }

    @DisplayName("answers 삭제")
    @Test
    void removeAnswers() throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = answers.deleteAnswers(user);
        assertThat(deleteHistories.size()).isEqualTo(1);
    }

    @DisplayName("validate owner answers")
    @Test
    void removeAnswersOwnerException() {
        assertThatThrownBy(() -> {
            final User otherUser = userRepository.save(UserTest.JAVAJIGI);
            answers.deleteAnswers(otherUser);
        }).isInstanceOf(CannotDeleteException.class);
    }

    @AfterEach
    void beforeFinish() {
        answerRepository.flush();
        questionRepository.flush();
        userRepository.flush();
    }
}