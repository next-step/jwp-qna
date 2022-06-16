package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.generator.AnswerGenerator;
import qna.generator.QuestionGenerator;
import qna.generator.UserGenerator;

@DisplayName("Domain:Answers")
class AnswersTest {

    @Test
    @DisplayName("답변 일급 컬렉션의 일괄 삭제 처리")
    public void deleteAll() throws CannotDeleteException {
        // Given
        final User questionWriter = UserGenerator.generateQuestionWriter();
        final Question question = QuestionGenerator.generateQuestion(questionWriter);
        AnswerGenerator.generateAnswer(questionWriter, question);
        AnswerGenerator.generateAnswer(questionWriter, question);
        AnswerGenerator.generateAnswer(questionWriter, question);

        // When
        Answers answers = new Answers(question.getAnswers());
        List<DeleteHistory> deleteHistories = answers.deleteAll(questionWriter);

        // Then
        assertAll(
            () -> assertThat(question.getAnswers())
                .allSatisfy(answer -> assertThat(answer.isDeleted()).isTrue()),
            () -> assertThat(deleteHistories).extracting("contentType")
                .hasSize(3)
                .containsOnly(ContentType.ANSWER)
        );
    }
}
