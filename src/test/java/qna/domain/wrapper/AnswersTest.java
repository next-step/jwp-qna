package qna.domain.wrapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;
import qna.domain.*;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AnswersTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private Question question;
    private User user;

    @BeforeEach
    void setup() {
        user = userRepository.save(UserTest.JAVAJIGI);
        question = questionRepository.save(new Question("title1", "contents1").writtenBy(user));
    }

    @AfterEach
    void deleteAll() {
        answerRepository.deleteAll();
    }

    @Test
    void 삭제하려는_유저의_answer_작성자_유효성_성공_Test() throws CannotDeleteException {
        Answers answers = new Answers(Arrays.asList(new Answer(1L, user, question, "contents1"),
                                                    new Answer(2L, user, question, "contents2")));

        answers.validateProprietary(user);
    }

    @Test
    void 삭제하려는_유저의_answer_작성자_유효성_실패_Test() {
        Answers answers = new Answers(Arrays.asList(new Answer(1L, UserTest.JAVAJIGI, question, "contents1"),
                                                    new Answer(2L, UserTest.SANJIGI, question, "contents2")));

        assertThatThrownBy(() ->
                answers.validateProprietary(UserTest.JAVAJIGI)
        ).isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void create() {
        Answers answers = new Answers(Arrays.asList(AnswerTest.A1,
                                                    AnswerTest.A2));

        assertThat(answers.answers()).contains(AnswerTest.A1, AnswerTest.A2);
    }

    @Test
    void deleteAll_Test() {
        Answers answers = new Answers(Arrays.asList(AnswerTest.A1,
                                                    AnswerTest.A2));

        answers.deleteAllAndHistories();

        assertThat(answers.answers().stream()
                                    .anyMatch(answer -> !answer.isDeleted())
        ).isFalse();
    }

}
