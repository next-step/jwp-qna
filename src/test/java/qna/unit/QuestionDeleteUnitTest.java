package qna.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import qna.CannotDeleteException;
import qna.UnAuthorizedException;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.UserTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class QuestionDeleteUnitTest {

    private Question question = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
    private Answer answer = new Answer(1L, UserTest.JAVAJIGI, question, "Answers Contents1");

    @BeforeEach
    public void setUp() {
        question.addAnswer(answer);
    }

    @Test
    void 질문데이터_삭제는_데이터의_상태를_변경() {
        assertThat(question.isDeleted()).isFalse();
        question.delete(UserTest.JAVAJIGI);

        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    void 로그인사용자_질문사용자_동일해야_삭제가능() {
        assertThat(question.delete(UserTest.JAVAJIGI)).isNotNull();
        assertThatThrownBy(() -> question.delete(UserTest.SANJIGI))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 질문사용자_모든답변의_답변자가_동일해야_삭제가능() {
        assertThat(question.delete(UserTest.JAVAJIGI)).isNotNull();
        question.addAnswer(new Answer(1L, UserTest.SANJIGI, question, "Answers Contents1"));

        assertThatThrownBy(() -> question.delete(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void 질문삭제시_답변도삭제하며_답변도_데이터의_상태를_변경() {
        assertThat(question.isAllDeletedAnswers()).isFalse();
        question.delete(UserTest.JAVAJIGI);

        assertThat(question.isAllDeletedAnswers()).isTrue();
    }

    @Test
    void 질문과답변_삭제이력은_내역을_관리() {
        assertThat(question.delete(UserTest.JAVAJIGI).getList().size()).isGreaterThan(0);
    }

}
