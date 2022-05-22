package qna.domain;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    void delete() throws CannotDeleteException {
        List<DeleteHistory> expect = Q1.delete(UserTest.JAVAJIGI);
        DeleteHistory result = new DeleteHistory(ContentType.QUESTION, Q1.getId(), Q1.getWriter(), LocalDateTime.now());

        assertAll(
                () -> assertThat(Q1.isDeleted()).isEqualTo(true),
                () -> assertThat(expect.get(0).getContentId()).isEqualTo(result.getContentId()),
                () -> assertThat(expect.get(0).getDeletedBy()).isEqualTo(result.getDeletedBy()),
                () -> assertThat(expect.get(0).getContentType()).isEqualTo(result.getContentType())
        );
    }

    @Test
    void delete_예외테스트() {
        assertThatThrownBy(() -> Q1.delete(UserTest.SANJIGI)).isInstanceOf(CannotDeleteException.class);
    }
}
