package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static qna.domain.UserTest.JAVAJIGI;

public class AnswerTest {
    public static final Answer A1 = new Answer(JAVAJIGI, QuestionTest.Q1, "Answers Contents1");

    @Test
    @DisplayName("Answer를 삭제한다")
    void Answer_삭제() {
        // when
        A1.delete(JAVAJIGI, LocalDateTime.now());
        // then
        assertThat(A1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("작성자와 일치하는지 검증한다.")
    void 작성자_일치_검증() {
        // then
        assertThat(A1.isOwner(JAVAJIGI)).isTrue();
    }
}
