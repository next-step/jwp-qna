package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {

    private Question question;
    private List<DeleteHistory> deleteHistories;

    @BeforeEach
    void setUp() {
        question = new Question();
        question.writeBy(UserRepositoryTest.JAVAJIGI);

        Answer answer = new Answer(1L, UserRepositoryTest.JAVAJIGI, question, "Answers Contents1");
        question.addAnswer(answer);

        deleteHistories = Arrays.asList(
                DeleteHistory.ofQuestion(question.getId(), question.getWriter()),
                DeleteHistory.ofAnswer(answer.getId(), answer.getWriter())
        );
    }

    @Test
    @DisplayName("질문을 삭제한다.")
    void delete_성공() throws CannotDeleteException {
        // given
        assertThat(question.isDeleted()).isFalse();

        // when
        List<DeleteHistory> result = question.delete(UserRepositoryTest.JAVAJIGI);

        // then
        assertThat(question.isDeleted()).isTrue();
        assertThat(result).isEqualTo(deleteHistories);
    }

    @Test
    @DisplayName("다른 사람이 쓴 글을 삭제할 시 실패한다.")
    void delete_다른_사람이_쓴_글() {
        // given
        assertThat(question.isDeleted()).isFalse();

        // when, then
        assertThatThrownBy(() -> question.delete(UserRepositoryTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("다른 사람이 쓴 답변이 존재할 경우 실패한다.")
    void delete_다른_사람이_쓴_답변_존재() {
        // given
        question.addAnswer(new Answer(2L, UserRepositoryTest.SANJIGI, question, "Answers Contents2"));

        // when, then
        assertThatThrownBy(() -> question.delete(UserRepositoryTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
