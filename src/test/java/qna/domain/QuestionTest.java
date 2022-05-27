package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static qna.domain.AnswerTest.A1;
import static qna.domain.UserTest.JAVAJIGI;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
    @Test
    @DisplayName("질문을 삭제한다")
    void 질문_삭제() {
        // given
        Q1.setDeleted(false);
        // when
        Q1.delete(JAVAJIGI, LocalDateTime.now());
        // then
        assertThat(Q1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문을 삭제한다")
    void 질문_답변_삭제() {
        // given
        Q1.setDeleted(false);
        A1.setDeleted(false);
        Q1.addAnswer(A1);
        // when
        Q1.deleteQuestionAndAnswer(JAVAJIGI);
        // then
        assertThat(Q1.isDeleted()).isTrue();
        assertThat(A1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("작성자와 일치하는지 검증한다.")
    void 작성자_일치_검증() {
        // then
        assertThat(Q1.isOwner(JAVAJIGI)).isTrue();
    }
}
