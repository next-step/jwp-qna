package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {
    private static final User userTest = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    private static final Question questionTest = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
    private static final Answer answerTest = new Answer(userTest, questionTest, "contents test");
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void init() {
        userRepository.save(userTest);
        questionRepository.save(questionTest);
        answerRepository.save(answerTest);
    }

    @Test
    void findByQuestionIdAndDeletedFalse() {
        final List<Answer> answers = answerRepository.findByQuestionAndDeletedFalse(questionTest);
        assertAll(
                () -> assertThat(answers).isNotEmpty(),
                () -> assertThat(answers.get(0).getQuestionId()).isEqualTo(questionTest.getId()),
                () -> assertThat(answers.get(0).getWriterId()).isEqualTo(questionTest.getWriterId()),
                () -> assertThat(answers.get(0).getContents()).isEqualTo("contents test")
        );
    }

    @Test
    void findByIdAndDeletedFalse() {
        final Answer answer = answerRepository.findByIdAndDeletedFalse(1L).orElse(answerTest);
        assertAll(
                () -> assertThat(answer).isNotNull(),
                () -> assertThat(answer.getQuestionId()).isEqualTo(questionTest.getId()),
                () -> assertThat(answer.getWriterId()).isEqualTo(userTest.getId()),
                () -> assertThat(answer.getContents()).isEqualTo("contents test")
        );
    }
}
