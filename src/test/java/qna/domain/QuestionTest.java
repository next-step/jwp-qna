package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("작성자가 질문을 삭제한다")
    void delete() throws CannotDeleteException {
        Question question = new Question(1L, "title1", "contents1", Answers.empty()).writeBy(UserTest.JAVAJIGI);
        List<DeleteHistory> deleteHistories = question.delete(UserTest.JAVAJIGI);
        List<DeleteHistory> expected = singletonList(DeleteHistory.ofQuestion(question.getId(), UserTest.JAVAJIGI));

        assertThat(deleteHistories).isEqualTo(expected);
    }

    @Test
    @DisplayName("작성자 외에 사용자가 질문을 삭제할 경우 예외가 발생한다")
    void delete_thrownIllegalArgument1() {
        Question question = new Question(1L, "title1", "contents1", Answers.empty()).writeBy(UserTest.JAVAJIGI);

        assertThatThrownBy(() -> question.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("질문을 삭제할 권한이 없습니다.");
    }
    @Test
    @DisplayName("다른 사람이 쓴 답변이 있을 경우 예외가 발생한다")
    void delete_thrownIllegalArgument2() {
        Answers answers = new Answers(singletonList(new Answer(1L, UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents1")));
        Question question = new Question(1L, "title1", "contents1", answers).writeBy(UserTest.JAVAJIGI);

        assertThatThrownBy(() -> question.delete(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
