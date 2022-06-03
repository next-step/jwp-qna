package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.generator.QuestionGenerator.generateQuestion;
import static qna.generator.UserGenerator.generateAnswerWriter;
import static qna.generator.UserGenerator.generateQuestionWriter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;

@DisplayName("Domain:Answer")
public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("답변 생성")
    public void createAnswerEntityTest() {
        // Given
        final String contents = "질문 답변";
        final Question question = generateQuestion(generateQuestionWriter());

        // When
        Answer given = new Answer(generateAnswerWriter(), question, contents);

        // Then
        assertAll(
            () -> assertThat(given.getId()).as("IDENTITY 전략에 의해 DB에서 생성되는 PK값의 Null 여부").isNull(),
            () -> assertThat(given.getContents()).isEqualTo(contents),
            () -> assertThat(given.getCreatedAt()).as("JPA Audit에 의해 할당되는 생성일시 정보의 Null 여부").isNull(),
            () -> assertThat(given.getUpdatedAt()).as("JPA Audit에 의해 할당되는 수정일시 정보의 Null 여부").isNull()
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
        assertThat(given.getQuestionId()).isEqualTo(question.getId());
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
