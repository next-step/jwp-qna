package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

    @Test
    @DisplayName("본인이 쓴 질문글은 삭제 가능하다")
    void delete() throws CannotDeleteException {
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        assertThat(question.isDeleted()).isFalse();
        List<DeleteHistory> delete = question.delete(UserTest.JAVAJIGI);
        assertThat(question.isDeleted()).isTrue();
        assertThat(delete).isNotNull();
    }

    @Test
    @DisplayName("다른 유저가 쓴 질문글은 삭제 시도시 예외가 발생한다")
    void deleteException() {
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        assertThatThrownBy(() -> question.delete(UserTest.SANJIGI)).isInstanceOf(CannotDeleteException.class)
                .hasMessage("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("본인이 쓴 질문글에 본인 답글만 있으면 삭제 가능하다")
    void deleteQuestionWithAnswers() throws CannotDeleteException {
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        question.addAnswer(new Answer(UserTest.JAVAJIGI, question, "c"));
        question.addAnswer(new Answer(UserTest.JAVAJIGI, question, "c2"));
        List<DeleteHistory> delete = question.delete(UserTest.JAVAJIGI);
        assertThat(delete).isNotNull();
    }

    @Test
    @DisplayName("본인 질문글에 다른 유저의 답글이 있을경우 삭제 시도시 예외가 발생한다")
    void deleteQuestionWithAnswersException() {
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        question.addAnswer(new Answer(UserTest.JAVAJIGI, question, "c"));
        question.addAnswer(new Answer(UserTest.IU, question, "c"));
        assertThatThrownBy(() -> question.delete(UserTest.JAVAJIGI)).isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
    @Test
    @DisplayName("이미 삭제한 질문글을 다시 삭제하면 예외가 발생한다")
    void deleteAgainException() throws CannotDeleteException {
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        question.delete(UserTest.JAVAJIGI);
        assertThatThrownBy(() -> question.delete(UserTest.JAVAJIGI)).isInstanceOf(CannotDeleteException.class)
                .hasMessage("이미 삭제한 질문입니다.");
    }
}
