package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.generator.AnswerGenerator;
import qna.generator.QuestionGenerator;
import qna.generator.UserGenerator;

@DisplayName("Domain:Answers")
class AnswersTest {

    @Test
    @DisplayName("일괄 삭제 처리")
    public void deleteAll() throws CannotDeleteException {
        // Given
        final User questionWriter = UserGenerator.generateQuestionWriter();
        final Question question = QuestionGenerator.generateQuestion(questionWriter);
        AnswerGenerator.generateAnswer(questionWriter, question);
        AnswerGenerator.generateAnswer(questionWriter, question);
        AnswerGenerator.generateAnswer(questionWriter, question);

        // When
        Answers answers = new Answers(question.getAnswers());
        answers.deleteAll(questionWriter);

        // Then
        assertThat(question.getAnswers())
            .allSatisfy(it -> assertThat(it.isDeleted()).isTrue());
    }
}
