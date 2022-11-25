package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;

@DisplayName("질문_관련_테스트")
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    private Question question1;

    @BeforeEach
    void before() {
        question1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    }

    @DisplayName("question만_존재할_때_삭제_성공")
    @Test
    void deleteContentsWhenOnlyQuestion() throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = question1.deleteQuestion(question1.getWriter());

        assertThat(deleteHistories.size()).isEqualTo(1);
    }

    @DisplayName("question_answer_모두_User가_작성했을_때_삭제_성공")
    @Test
    void deleteContentsWhenQuestionAndAnswerWrittenByUser() throws CannotDeleteException {
        question1.addAnswer(AnswerTest.A1);

        List<DeleteHistory> deleteHistories = question1.deleteQuestion(question1.getWriter());

        assertThat(deleteHistories.size()).isEqualTo(2);
    }

    @DisplayName("다른_User가_작성한_question_삭제하려_할때_실패")
    @Test
    void deleteContentsThrowErrorWhenQuestionWrittenByAnotherUser() {
        question1.writeBy(UserTest.JAVAJIGI);
        assertThatThrownBy(() -> question1.deleteQuestion(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }


    @DisplayName("answer중_다른_User가_작성했을_때_삭제_실패")
    @Test
    void deleteContentsThrowErrorWhenAnswerWrittenByAnotherUser() {
        question1.writeBy(UserTest.JAVAJIGI);
        question1.addAnswer(AnswerTest.A2);
        assertThatThrownBy(() -> question1.deleteQuestion(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }
}
