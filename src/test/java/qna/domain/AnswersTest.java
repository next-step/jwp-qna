package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.AnswerTest.A1;

public class AnswersTest {
    @DisplayName("Answers 생성 테스트")
    @Test
    void createAnswers() {
        Answers answers = new Answers(Arrays.asList(A1));
        assertThat(answers.countAnswerOfOwner(UserTest.JAVAJIGI)).isEqualTo(1L);
    }

    @DisplayName("Answers 권한 예외처리")
    @Test
    void isOwnerOfAnswers() {
        Answers answers = new Answers(Arrays.asList(A1));
        assertThatThrownBy(() -> answers.checkAuthorityDeleteAnswers(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }
}
