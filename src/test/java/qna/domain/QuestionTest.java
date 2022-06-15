package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.generator.UserGenerator.generateAnswerWriter;
import static qna.generator.UserGenerator.generateLoginUser;
import static qna.generator.UserGenerator.generateQuestionWriter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.generator.AnswerGenerator;
import qna.generator.QuestionGenerator;

@DisplayName("Domain:Question")
public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("질문 생성")
    public void createQuestionEntityTest() {
        // Given
        final String contents = "질문 내용";
        final String title = "질문 제목";

        // When
        Question given = new Question(title, contents).writeBy(generateQuestionWriter());

        // Then
        assertAll(
            () -> assertThat(given).isEqualTo(new Question(title, contents).writeBy(generateQuestionWriter())),
            () -> assertThat(given.getTitle()).isEqualTo(title),
            () -> assertThat(given.getContents()).isEqualTo(contents)
        );
    }

    @Test
    @DisplayName("질문 삭제")
    public void delete() throws CannotDeleteException {
        // Given
        final User loginUser = generateLoginUser();
        final Question question = QuestionGenerator.generateQuestion(loginUser);
        AnswerGenerator.generateAnswer(loginUser, question);

        // When
        question.delete(loginUser);

        // Then
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문 삭제 시 예외 : 질문 삭제 요청자와 질문 작성자가 다른 경우")
    public void throwException_LoginUserIsNotQuestionWriter() {
        // Given
        final User loginUser = generateLoginUser();
        final User questionWriter = generateQuestionWriter();
        final Question question = QuestionGenerator.generateQuestion(questionWriter);
        final User answerWriter = generateAnswerWriter();
        AnswerGenerator.generateAnswer(questionWriter, question);
        AnswerGenerator.generateAnswer(questionWriter, question);
        AnswerGenerator.generateAnswer(answerWriter, question);

        // When & Then
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() -> question.delete(loginUser));
    }

    @Test
    @DisplayName("질문 삭제 시 예외 : 질문 작성자와 답변 작성자가 다른 경우")
    public void throwException_QuestionWriterIsNotAnswerWriter() {
        // Given
        final User loginUser = generateLoginUser();
        final User questionWriter = generateQuestionWriter();
        final Question question = QuestionGenerator.generateQuestion(questionWriter);
        final User answerWriter = generateAnswerWriter();
        AnswerGenerator.generateAnswer(questionWriter, question);
        AnswerGenerator.generateAnswer(questionWriter, question);
        AnswerGenerator.generateAnswer(answerWriter, question);

        // When & Then
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() -> question.delete(loginUser));
    }
}
