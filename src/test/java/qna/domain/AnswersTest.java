package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.fixture.QuestionTestFixture;
import qna.fixture.UserTestFixture;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnswersTest {
    @Test
    @DisplayName("질문 작성자와 모든 답변의 작성자가 같지 않으면 답변 삭제 불가")
    void 질문_작성자와_모든_답변의_작성자가_같지_않으면_답변_삭제_불가() {
        User questionWriter = UserTestFixture.JAVAJIGI;
        User differentUser = UserTestFixture.SANJIGI;
        Answers answers = new Answers(Arrays.asList(
                new Answer(differentUser, QuestionTestFixture.Q1, "contents"),
                new Answer(differentUser, QuestionTestFixture.Q1, "contents")
        ));

        assertThatThrownBy(() -> answers.deleteAll(questionWriter))
                .isInstanceOf(CannotDeleteException.class);
    }
}
