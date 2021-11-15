package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DeleteHistoriesTest {
    @DisplayName("Question 에 속한 Answers 까지 모든 이력을 가져온다.")
    @Test
    void create() {
        // given
        User writer = UserTest.JAVAJIGI;
        Question question = QuestionTest.createQuestion("question", "questionContents", writer);
        Answer firstAnswer = AnswerTest.createAnswer(writer, question);
        Answer secondAnswer = AnswerTest.createAnswer(writer, question);

        // when
        DeleteHistories deleteHistories = new DeleteHistories(question);
        List<DeleteHistory> histories = deleteHistories.getDeleteHistories();

        // then
        assertEquals(3, histories.size());
        assertThat(histories).extracting("contentType")
            .containsExactly(ContentType.QUESTION, ContentType.ANSWER, ContentType.ANSWER);
        assertThat(histories).extracting("contentId")
            .containsExactly(question.getId(), firstAnswer.getId(), secondAnswer.getId());
    }
}
