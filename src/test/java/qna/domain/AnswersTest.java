package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static qna.domain.QuestionTestFixture.Q1;
import static qna.domain.QuestionTestFixture.Q2;
import static qna.domain.UserTestFixture.JAVAJIGI;
import static qna.domain.UserTestFixture.SANJIGI;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

class AnswersTest {
    @Test
    @DisplayName("답변이 없는지 확인한다.")
    void answers_is_empty() {
        Answers answers = new Answers();
        assertTrue(answers.isEmpty());
    }

    @Test
    @DisplayName("답변을 추가한다")
    void add_answer() {
        Answers answers = new Answers();
        answers.addAnswer(new Answer(JAVAJIGI, Q1, "contents"));
        assertThat(answers.getAnswers()).contains(new Answer(JAVAJIGI, Q1, "contents"));
    }

    @Test
    @DisplayName("답변을 삭제할때 작성자가 다르면 CannotDeleteException 예외 던지기")
    void delete_answers_by_different_user_throw_CannotDeleteException() {
        Answers answers = new Answers();
        answers.addAnswer(new Answer(JAVAJIGI, Q1, "contents"));
        answers.addAnswer(new Answer(SANJIGI, Q2, "contents"));
        assertThatThrownBy(() -> answers.delete(JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
