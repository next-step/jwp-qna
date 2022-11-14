package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.common.exception.CannotDeleteException;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = Question.of("title1", "contents1").writeBy(1L);
    public static final Question Q2 = Question.of("title2", "contents2").writeBy(2L);

    @Test
    void 값_검증() {
        assertAll(
                () -> assertThat(Q1.getTitle()).isEqualTo("title1"),
                () -> assertThat(Q2.getTitle()).isEqualTo("title2"),
                () -> assertThat(Q1.getContents()).isEqualTo("contents1"),
                () -> assertThat(Q2.getContents()).isEqualTo("contents2"),
                () -> assertThat(Q1.getWriterId()).isEqualTo(UserTest.JAVAJIGI.getId()),
                () -> assertThat(Q2.getWriterId()).isEqualTo(UserTest.SANJIGI.getId())
        );
    }

    @Test
    void 삭제() {
        Q1.setDeleted();
        assertThat(Q1.isDeleted()).isTrue();
    }

    @Test
    void 삭제_권한() {
        assertThatThrownBy(() -> Q1.isOwner(UserTest.SANJIGI)).isInstanceOf(CannotDeleteException.class);
    }
}
