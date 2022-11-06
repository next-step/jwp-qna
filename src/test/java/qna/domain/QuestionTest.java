package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.FixtureUtils.*;

import org.junit.jupiter.api.Test;

public class QuestionTest {
    @Test
    void 동등성() {
        assertAll(
            () -> assertThat(Q1()).isEqualTo(Q1()),
            () -> assertThat(Q1()).isNotEqualTo(Q2())
        );
    }

    @Test
    void 작성자_등록() {
        assertAll(
            () -> assertThat(Q1().isOwner(JAVAJIGI())).isTrue(),
            () -> assertThat(Q1().isOwner(SANJIGI())).isFalse()
        );
    }

    @Test
    void 답변_추가() {
        User JAVAJIGI = JAVAJIGI();
        Question q1 = Q1(JAVAJIGI);
        Answer answer = A1(JAVAJIGI, q1);
        q1.addAnswer(answer);

        assertAll(
            () -> assertThat(q1.getAnswers()).contains(answer),
            () -> assertThat(answer.getQuestion()).isEqualTo(q1)
        );
    }
}
