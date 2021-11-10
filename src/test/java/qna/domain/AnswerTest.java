package qna.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    private AnswerRepository answerRepository;

    @Autowired
    public AnswerTest(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @ParameterizedTest
    @MethodSource("testAnswerList")
    void testEquals(Answer answer) {
        Answer savedAnswer = answerRepository.save(answer);
        assertThat(savedAnswer.getId()).isNotNull();
        assertThat(savedAnswer.getWriterId()).isEqualTo(answer.getWriterId());
        assertThat(savedAnswer.getQuestionId()).isEqualTo(answer.getQuestionId());
        assertThat(savedAnswer.getContents()).isEqualTo(answer.getContents());
        assertThat(savedAnswer.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
        assertThat(savedAnswer.getUpdatedAt()).isNull();
    }

    private static List<Answer> testAnswerList() {
        return Arrays.asList(A1, A2);
    }
}
