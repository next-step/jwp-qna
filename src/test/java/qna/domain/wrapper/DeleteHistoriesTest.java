package qna.domain.wrapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.domain.Answer;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.User;
import qna.domain.UserTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DeleteHistoriesTest {

    private Question savedQuestion;
    private User savedUser;

    @BeforeEach
    void setUp() {
        this.savedUser = UserTest.JAVAJIGI;
        Question question = new Question("testTitle", "testContents", UserTest.JAVAJIGI);
        Answer answer = new Answer(1L, savedUser, question, "testContents");
        question.addAnswer(answer);
        this.savedQuestion = question;
    }

    @Test
    void 질문자_답변글_모든_답변자_다를시_삭제불가능() {
        // given
        Answer answer = new Answer(2L, UserTest.SANJIGI, this.savedQuestion, "testContents");
        this.savedQuestion.addAnswer(answer);

        // when & then
        assertThatThrownBy(() -> DeleteHistories.of(savedUser, savedQuestion))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void 질문_답변_삭제이력_반환() {
        // given
        DeleteHistories deleteHistories = DeleteHistories.of(savedUser, savedQuestion);

        // when
        List<DeleteHistory> deleteHistoryList = deleteHistories.getDeleteHistories();

        // then
        assertThat(deleteHistoryList).hasSize(2);
    }
}
