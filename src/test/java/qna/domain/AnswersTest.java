package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswersTest {
    Question question;
    Answers answers = new Answers();

    @BeforeEach
    void set() {
        question = new Question("title", "contents").writeBy(UserTest.JAVAJIGI);
        answers.add(new Answer(UserTest.JAVAJIGI, question, "contests1"));
        answers.add(new Answer(UserTest.JAVAJIGI, question, "contests2"));
    }

    @Test
    @DisplayName("본인의 댓글만 있으면 삭제가 가능하다")
    void deleteAnswers() throws CannotDeleteException {
        List<DeleteHistory> delete = answers.delete(UserTest.JAVAJIGI);
        assertThat(delete).hasSize(2);
    }

    @Test
    @DisplayName("다른 사람의 댓글이 있는 경우 삭제 시도시 예외가 발생한다")
    void deleteAnswersException() {
        answers.add(new Answer(UserTest.SANJIGI, question, "new contents"));
        assertThatThrownBy(() -> answers.delete(UserTest.JAVAJIGI)).isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
