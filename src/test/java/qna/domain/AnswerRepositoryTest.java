package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @BeforeEach
    void init() {
        answerRepository.saveAll(
                Arrays.asList(AnswerTest.A1, AnswerTest.A2, AnswerTest.A3)
        );
    }

    @Test
    void save() {
        final Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        assertThat(answer.getQuestion()).isEqualTo(QuestionTest.Q1);
        assertThat(answer.getWriter()).isEqualTo(UserTest.JAVAJIGI);
    }

    @Test
    void findByQuestionIdAndDeletedFalse() {
        AnswerTest.A1.setDeleted(false);
        AnswerTest.A2.setDeleted(false);

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());

        assertThat(answers).contains(AnswerTest.A1, AnswerTest.A2);
    }

    @Test
    void findByIdAndDeletedFalse() {
        AnswerTest.A1.setDeleted(false);

        final Answer answer = answerRepository.findByIdAndDeletedFalse(AnswerTest.A1.getId()).get();

        assertThat(answer).isEqualTo(AnswerTest.A1);
    }

}
