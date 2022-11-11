package qna.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("질문 데이터를 완전히 삭제하는 것이 아니라 데이터의 상태를 삭제 상태로 변경한다.")
    void question_soft_delete() {
        assertThat(question.isDeleted()).isFalse();
        question.delete(UserTest.JAVAJIGI);

        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("로그인 사용자와 질문한 사람이 같은 경우 삭제할 수 있다.")
    void writer_must_same_requester() {
        assertThat(question.delete(UserTest.JAVAJIGI)).isNotNull();
        assertThatThrownBy(() -> question.delete(UserTest.SANJIGI))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("질문자와 답변 글의 모든 답변자 같은 경우 삭제가 가능하다.")
    void answer_writers_must_same_question_writer() {
        question.addAnswer(new Answer(1L, UserTest.SANJIGI, question, "Answers Contents1"));

        assertThatThrownBy(() -> question.delete(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태를 변경한다.")
    void answer_soft_delete() {
        assertThat(question.isAllDeletedAnswers()).isFalse();
        question.delete(UserTest.JAVAJIGI);

        assertThat(question.isAllDeletedAnswers()).isTrue();
    }

    @Test
    @DisplayName("질문과 답변 삭제 이력에 대한 정보를 DeleteHistory를 활용해 남긴다.")
    void must_add_delete_history() {
        assertThat(question.delete(UserTest.JAVAJIGI).getList()).isNotEmpty();
    }

}
