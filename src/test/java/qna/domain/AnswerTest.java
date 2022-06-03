package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.generator.QuestionGenerator.generateQuestion;
import static qna.generator.UserGenerator.generateAnswerWriter;
import static qna.generator.UserGenerator.generateQuestionWriter;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import qna.UnAuthorizedException;

@DisplayName("Domain:Answer")
public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @ParameterizedTest
    @MethodSource
    @DisplayName("ID 필드를 이용한 객체 동등성 비교")
    public void answerEntityEqualsTest(
        final Answer given,
        final Answer actual,
        final boolean expected,
        final String testDescription
    ) {
        // Then
        assertThat(given.equals(actual)).as(testDescription).isEqualTo(expected);
    }

    private static Stream answerEntityEqualsTest() {
        final String contents = "답변 내용";
        final Question question = generateQuestion(generateQuestionWriter());
        final User answerWriter = generateAnswerWriter();

        return Stream.of(
            Arguments.of(
                new Answer(null, answerWriter, question, contents),
                new Answer(null, answerWriter, question, contents),
                true,
                "ID 필드가 Null이고 다른 필드의 값이 같은 객체의 동등성 비교"
            ),
            Arguments.of(
                new Answer(null, answerWriter, question, contents),
                new Answer(null, answerWriter, question, "답변 내용 2"),
                true,
                "ID 필드가 Null이고 특정 필드의 값이 다른 객체의 동등성 비교"
            ),
            Arguments.of(
                new Answer(1L, answerWriter, question, contents),
                new Answer(2L, answerWriter, question, contents),
                false,
                "ID 필드의 값이 다른 객체의 동등성 비교"
            ),
            Arguments.of(
                new Answer(1L, answerWriter, question, contents),
                new Answer(1L, answerWriter, question, contents),
                true,
                "ID 필드의 값과 다른 필드의 값이 같은 객체의 동등성 비교"
            ),
            Arguments.of(
                new Answer(1L, answerWriter, question, contents),
                new Answer(1L, answerWriter, question, "답변 내용 2"),
                true,
                "ID 필드의 값이 같고, 특정 필드의 값이 다른 객체의 동등성 비교"
            )
        );
    }

    @Test
    @DisplayName("답변 추가")
    public void toQuestion() {
        // Given
        final String contents = "질문 답변";
        final Question question = generateQuestion(generateQuestionWriter());
        final Answer given = new Answer(generateAnswerWriter(), question, contents);

        // When
        given.toQuestion(question);

        // Then
        assertAll(
            () -> assertThat(given.getQuestionId()).isEqualTo(question.getId()),
            () -> assertThat(question.getId()).isNull()
        );
    }

    @Test
    @DisplayName("작성자 정보가 Null인 경우 예외")
    public void throwException_WhenAnswerWriterIsNull() {
        // Given
        final Question question = generateQuestion(generateQuestionWriter());

        // When & Then
        assertThatExceptionOfType(UnAuthorizedException.class)
            .isThrownBy(() -> new Answer(null, question, "답변내용"));
    }
}
