package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.fixture.UserTestFixture;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    @Test
    @DisplayName("질문 등록자와 로그인 사용자가 다르면 삭제 불가")
    void 질문_등록자와_로그인_사용자가_다르면_삭제_불가() {
        User questionWriter = UserTestFixture.JAVAJIGI;
        User loginUser = UserTestFixture.SANJIGI;
        Question question = new Question(1L, questionWriter, "title", "contents");

        assertThatThrownBy(() -> question.delete(loginUser))
                .isInstanceOf(CannotDeleteException.class);
    }
}
