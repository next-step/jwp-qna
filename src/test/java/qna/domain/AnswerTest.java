package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {

    private Answer answer;

    @BeforeEach
    void setUp() {
        Question question = new Question();
        question.writeBy(UserRepositoryTest.JAVAJIGI);
        answer = new Answer(1L, UserRepositoryTest.JAVAJIGI, question, "Answers Contents1");
    }

    @Test
    @DisplayName("답변을 삭제한다.")
    void delete_성공() throws CannotDeleteException {
        // given
        assertThat(answer.isDeleted()).isFalse();

        // when
        answer.delete(UserRepositoryTest.JAVAJIGI);

        // then
        assertThat(answer.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("답변을 삭제한다.")
    void delete_다른_사람이_쓴_답변() throws CannotDeleteException {
        // given
        assertThat(answer.isDeleted()).isFalse();

        // when, then
        assertThatThrownBy(() -> answer.delete(UserRepositoryTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
