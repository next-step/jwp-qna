package qna.domain.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import qna.CannotDeleteException;
import qna.domain.entity.*;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    private User user1;
    private User user2;

    private Question question1;
    private Question question2;

    private Answer answer1;
    private Answer answer2;

    @BeforeEach
    public void setUp() {
        user1 = userRepository.save(UserTest.USER_JAVAJIGI);
        user2 = userRepository.save(UserTest.USER_SANJIGI);

        question1 = questionRepository.save(QuestionTest.QUESTION_OF_JAVAJIGI);
        question2 = questionRepository.save(QuestionTest.QUESTION_OF_SANJIGI);

        answer1 = answerRepository.save(AnswerTest.ANSWER_OF_JAVAJIGI);
        answer2 = answerRepository.save(AnswerTest.ANSWER_OF_SANJIGI);
    }

    @Test
    public void exists() {
        assertAll(
            () -> assertThat(answer1.getId()).isNotNull(),
            () -> assertThat(answer2.getId()).isNotNull(),
            () -> assertThat(answer1.isEqualQuestion(question1)).isTrue(),
            () -> assertThat(answer2.isEqualQuestion(question2)).isTrue(),
            () -> assertThat(answer1.isOwner(user1)).isTrue(),
            () -> assertThat(answer2.isOwner(user2)).isTrue()
        );
    }

    @Test
    @DisplayName("동등성 비교")
    public void isEqualTo() {
        assertAll(
            () -> assertThat(answer1).isEqualTo(AnswerTest.ANSWER_OF_JAVAJIGI),
            () -> assertThat(answer2).isEqualTo(AnswerTest.ANSWER_OF_SANJIGI)
        );
    }

    @Test
    public void findByQuestionIdAndDeletedFalse() {
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question1.getId());

        assertAll(
            () -> assertThat(answers.size()).isEqualTo(1),
            () -> assertThat(answers).contains(answer1)
        );
    }

    @Test
    public void findByQuestionIdAndDeletedFalse2() throws CannotDeleteException {
        answer2.deleted(user2);

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question1.getId());

        assertAll(
            () -> assertThat(answers.size()).isEqualTo(1),
            () -> assertThat(answers).contains(answer1),
            () -> assertThat(answers).doesNotContain(answer2)
        );
    }

    @Test
    public void findByIdAndDeletedFalse() {
        Optional<Answer> answerOptional = answerRepository.findByIdAndDeletedFalse(answer1.getId());

        assertAll(
            () -> assertThat(answerOptional).isNotEmpty(),
            () -> assertThat(answerOptional.get()).isEqualTo(answer1)
        );
    }
}
