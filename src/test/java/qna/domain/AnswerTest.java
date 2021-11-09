package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(JAVAJIGI, Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(SANJIGI, Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    private static Stream<Arguments> provideAnswers() {
        return Stream.of(
            Arguments.of(A1),
            Arguments.of(A2)
        );
    }

    @DisplayName("Answer객체룰 입력으로 받는 save통하여 저장한 후 조회하면, 결과의 속성과 입력객체의 속성은 동일하다.")
    @ParameterizedTest
    @MethodSource("provideAnswers")
    void saveTest(Answer expected) {
        Answer actual = answerRepository.save(expected);
        assertAll(
            () -> assertThat(actual.getWriterId()).isEqualTo(expected.getWriterId()),
            () -> assertThat(actual.getQuestionId()).isEqualTo(expected.getQuestionId()),
            () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }
}
