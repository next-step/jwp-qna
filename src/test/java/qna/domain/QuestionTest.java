package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.FixtureUtils.*;

import org.junit.jupiter.api.Test;

public class QuestionTest {
    @Test
    void 동등성() {
        assertAll(
            () -> assertThat(QUESTION1_WRITE_BY_JAVAJIGI()).isEqualTo(QUESTION1_WRITE_BY_JAVAJIGI()),
            () -> assertThat(QUESTION1_WRITE_BY_JAVAJIGI()).isNotEqualTo(QUESTION2_WRITE_BY_JAVAJIGI())
        );
    }

    @Test
    void 작성자_등록() {
        assertAll(
            () -> assertThat(QUESTION1_WRITE_BY_JAVAJIGI().isOwner(JAVAJIGI())).isTrue(),
            () -> assertThat(QUESTION1_WRITE_BY_JAVAJIGI().isOwner(SANJIGI())).isFalse()
        );
    }

    @Test
    void 답변_추가() {
        User JAVAJIGI = JAVAJIGI();
        Question q1 = QUESTION1(JAVAJIGI);
        Answer answer = ANSWER1(JAVAJIGI, q1);
        q1.addAnswer(answer);

        assertAll(
            () -> assertThat(q1.getAnswers()).contains(answer),
            () -> assertThat(answer.getQuestion()).isEqualTo(q1)
        );
    }
}
