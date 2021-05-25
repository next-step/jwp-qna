package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@SpringBootTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @AfterEach
    public void deleteAll() {
        answerRepository.deleteAll();
    }

    @Test
    void findByIdAndDeletedFalse_Test() {
        Answer expected = answerRepository.save(A1);
        Answer actual = answerRepository.findByIdAndDeletedFalse(expected.getId()).get();

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByQuestionIdAndDeletedFalse_Test() {
        Answer expected1 = answerRepository.save(A1);
        Answer expected2 = answerRepository.save(A2);
        List<Answer> actualList = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());

        assertThat(actualList).contains(expected1, expected2);
    }

    @Test
    void save() {
        Answer expected = A2;
        Answer actual = answerRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriterId()).isEqualTo(expected.getWriterId()),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(actual.getQuestionId()).isEqualTo(expected.getQuestionId())
        );
    }

    @Test
    void findById() {
        Answer expected = answerRepository.save(A1);
        Answer actual = answerRepository.findById(expected.getId()).get();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void delete() {

    }
}
