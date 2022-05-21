package qna.domain;

import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void 생성자_테스트_User_null_일때() {
        assertThatThrownBy(() -> new Answer(null, QuestionTest.Q1, "cotents"))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 생성자_테스트_Question_null_일때() {
        assertThatThrownBy(() -> new Answer(UserTest.JAVAJIGI, null, "cotents"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void isOwner_테스트() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "cotents");

        assertAll(
                () -> assertThat(answer.isOwner(UserTest.JAVAJIGI)).isTrue(),
                () -> assertThat(answer.isOwner(UserTest.SANJIGI)).isFalse()
        );
    }
}
