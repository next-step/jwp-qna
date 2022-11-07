package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.fixture.UserTestFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QuestionTest {
    @Test
    @DisplayName("질문 등록자와 로그인 사용자가 다르면 삭제 불가")
    void 질문_등록자와_로그인_사용자가_다르면_삭제_불가() {
        User questionWriter = UserTestFixture.JAVAJIGI;
        User loginUser = UserTestFixture.SANJIGI;
        Question question = new Question(1L, questionWriter, "title", "contents");

        assertThatThrownBy(() -> question.delete(loginUser))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("질문 삭제시 답변도 삭제")
    void 질문_삭제시_답변도_삭제() throws CannotDeleteException {
        User writer = UserTestFixture.JAVAJIGI;
        Question question = new Question(1L, writer, "title", "contents");
        Answer answer1 = new Answer(1L, writer, question, "contents1");
        Answer answer2 = new Answer(2L, writer, question, "contents2");
        question.addAnswer(answer1);
        question.addAnswer(answer2);

        assertThat(question.isDeleted()).isFalse();
        assertThat(answer1.isDeleted()).isFalse();
        assertThat(answer2.isDeleted()).isFalse();

        question.delete(writer);

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer1.isDeleted()).isTrue();
        assertThat(answer2.isDeleted()).isTrue();
    }
}
