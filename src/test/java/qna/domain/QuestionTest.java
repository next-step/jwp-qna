package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {

    private Question question;

    @BeforeEach
    void setUp() {
        question = new Question();
        question.writeBy(UserRepositoryTest.JAVAJIGI);
    }

    @Test
    @DisplayName("질문을 삭제한다.")
    void delete_성공() throws CannotDeleteException {
        // given
        assertThat(question.isDeleted()).isFalse();

        // when
        question.delete(UserRepositoryTest.JAVAJIGI);

        // then
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("다른 사람이 쓴 글을 삭제할 시 실패한다.")
    void delete_다른_사람이_쓴_글() {
        // given
        assertThat(question.isDeleted()).isFalse();

        // when, then
        assertThatThrownBy(() -> question.delete(UserRepositoryTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }
}
