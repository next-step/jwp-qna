package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnswersTest {

    @DisplayName("답변 전체 삭제 : 로그인 사용자와 답변 작성자가 같을 경우 성공")
    @Test
    void deleteAllBySameUser() throws CannotDeleteException {
        User loginUser = UserTest.JAVAJIGI;
        Answers answers = Answers.from(Arrays.asList(AnswerTest.A1));
        answers.deleteAll(loginUser);
        assertThat(answers.getAnswers().get(0).isDeleted()).isTrue();
    }

    @DisplayName("답변 전체 삭제 : 로그인 사용자와 답변 작성자가 다를 경우 예외 발생")
    @Test
    void deleteAllByWrongUser() {
        User loginUser = UserTest.JAVAJIGI;
        Answers answers = Answers.from(Arrays.asList(AnswerTest.A1, AnswerTest.A2));
        assertThatThrownBy(() -> answers.deleteAll(loginUser))
                .isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("답변 전체 삭제 : 답변이 없을 경우 아무 것도 하지 않음.")
    @Test
    void deleteNoAnswers() throws CannotDeleteException {
        User loinUser = UserTest.JAVAJIGI;
        Answers answers = Answers.from(null);
        answers.deleteAll(loinUser);
    }

}