package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class AnswerTest {

    @Test
    @DisplayName("로그인 사용자와 답변한 사람이 같은 경우 답변을 삭제할 수 있고 삭제 시 삭제 이력이 남는다.")
    void deleteAnswer_정상_답변자_일치() throws CannotDeleteException {
        final Answer answer = 답변_생성("answerWriter");

        final DeleteHistory deleteHistory = 답변_삭제(answer);

        assertAll(
                () -> assertThat(answer.isDeleted()).isTrue(),
                () -> assertThat(deleteHistory.getDeletedBy()).isEqualTo(answer.getWriter()),
                () -> assertThat(deleteHistory.getCreateDate()).isNotNull(),
                () -> assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.ANSWER)
        );
    }


    @Test
    @DisplayName("로그인 사용자와 답변한 사람이 다른 경우 해당 답변을 삭제할 수 없다.")
    void deleteAnswer_예외_답변자_불일치() {
        final User user = 사용자_생성("anotherWriter");
        final Answer answer = 답변_생성("answerWriter");

        assertThatThrownBy(() -> answer.deleteAnswer(user))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("답변을 삭제할 권한이 없습니다.");
    }

    private User 사용자_생성(String writer) {
        return new User(writer, "password", "lsh", "lsh@mail.com");
    }

    private Question 질문_생성(String writer) {
        return new Question("title", "contents", 사용자_생성(writer));
    }

    private Answer 답변_생성(String writer) {
        return new Answer(사용자_생성(writer), 질문_생성(writer), "contents");
    }

    private DeleteHistory 답변_삭제(Answer answer) {
        return answer.deleteAnswer(answer.getWriter());
    }
}
