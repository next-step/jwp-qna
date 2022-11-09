package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answers;

    @Autowired
    private TestEntityManager manager;

    private static Stream<Answer> testFixtureProvider() {
        return Stream.of(A1, A2);
    }

    @ParameterizedTest
    @MethodSource("testFixtureProvider")
    void find(final Answer answer) {
        final Answer saved = answers.save(answer);
        final Answer actual = answers.findById(saved.getId()).get();
        assertThat(actual).isEqualTo(saved);
        assertThat(actual).isSameAs(saved);
        flushAndClear();
    }

    @ParameterizedTest
    @MethodSource("testFixtureProvider")
    void update(final Answer answer) {
        final Answer saved = answers.save(answer);

        Answer updated = answers.findById(saved.getId()).get();
        updated.toQuestion(QuestionTest.Q2);

        final Answer actual = answers.findById(saved.getId()).get();
        assertThat(actual.getQuestionId()).isEqualTo(QuestionTest.Q2.getId());
        assertThat(actual).isSameAs(updated);
        flushAndClear();
    }

    @ParameterizedTest
    @MethodSource("testFixtureProvider")
    void delete(final Answer answer) {
        final Answer saved = answers.save(answer);

        answers.deleteById(saved.getId());
        assertThat(answers.findById(saved.getId()).orElse(null)).isNull();
        flushAndClear();
    }

    private void flushAndClear() {
        manager.flush();
        manager.clear();
    }
}
