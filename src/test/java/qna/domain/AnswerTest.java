package qna.domain;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void delete() throws CannotDeleteException {
        LocalDateTime deletedAt = LocalDateTime.now();

        DeleteHistory expect = A1.delete(UserTest.JAVAJIGI, deletedAt);
        DeleteHistory result = new DeleteHistory(ContentType.ANSWER, A1.getId(), A1.getWriter(), deletedAt);

        assertAll(
                () -> assertThat(A1.isDeleted()).isEqualTo(true),
                () -> assertThat(expect.getContentId()).isEqualTo(result.getContentId()),
                () -> assertThat(expect.getDeletedBy()).isEqualTo(result.getDeletedBy()),
                () -> assertThat(expect.getContentType()).isEqualTo(result.getContentType())
        );
    }

    @Test
    void delete_예외테스트() {
        assertThatThrownBy(() -> A1.delete(UserTest.SANJIGI, LocalDateTime.now())).isInstanceOf(CannotDeleteException.class);
    }
}
