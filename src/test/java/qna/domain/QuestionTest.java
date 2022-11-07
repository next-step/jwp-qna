package qna.domain;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.fixture.TestFixture.JAVAJIGI;
import static qna.fixture.TestFixture.Q1;
import static qna.fixture.TestFixture.SANJIGI;

class QuestionTest {

    @Test
    void writeBy() {
        Question question = Q1.writeBy(JAVAJIGI);

        assertThat(question.getWriter()).isEqualTo(JAVAJIGI);
    }

    @Test
    void deleteContentsOf() throws CannotDeleteException {
        Question question = Q1.writeBy(JAVAJIGI);

        List<DeleteHistory> deleteHistories = question.deleteContentsOf(JAVAJIGI);

        assertThat(deleteHistories).hasSize(1);
    }

    @Test
    void deleteContentsOf_QuestionCannotDeleteException() {
        Question question = Q1.writeBy(JAVAJIGI);

        assertThatThrownBy(() -> question.deleteContentsOf(SANJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    void deleteContentsOf_AnswerCannotDeleteException() {
        Question question = Q1.writeBy(JAVAJIGI);
        question.addAnswer(new Answer(1L, SANJIGI, Q1, "answer_contents"));

        assertThatThrownBy(() -> question.deleteContentsOf(SANJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("질문을 삭제할 권한이 없습니다.");
    }
}
