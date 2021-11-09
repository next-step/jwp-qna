package qna.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    private static Stream<Arguments> providerAnswers() {
        return Stream.of(
                Arguments.of(A1),
                Arguments.of(A2)
        );
    }

    @ParameterizedTest
    @MethodSource("providerAnswers")
    public void 답변_생성(Answer excepted) {
        Answer actual = answerRepository.save(excepted);

        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(excepted.getId()),
                () -> assertThat(actual.getWriterId()).isEqualTo(excepted.getWriterId()),
                () -> assertThat(actual.getContents()).isEqualTo(excepted.getContents()),
                () -> assertThat(actual.getQuestionId()).isEqualTo(excepted.getQuestionId())
        );
    }

}
