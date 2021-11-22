package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.domain.fixture.TestAnswerFactory;
import qna.domain.fixture.TestQuestionFactory;
import qna.domain.fixture.TestUserFactory;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class AnswerTest {

    @Test
    @DisplayName("로그인 사용자와 답변한 사람이 같은 경우 답변을 삭제할 수 있고 삭제 시 삭제 이력이 남는다.")
    void deleteAnswer_정상_답변자_일치() throws CannotDeleteException {
        final User user = TestUserFactory.create("writer");
        final Question question = TestQuestionFactory.create(user);
        final Answer answer = TestAnswerFactory.create(user, question);

        final DeleteHistory deleteHistory = 답변_삭제(answer, LocalDateTime.now());

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
        final User user = TestUserFactory.create("writer");
        final Question question = TestQuestionFactory.create(user);
        final Answer answer = TestAnswerFactory.create(user, question);
        final User answerWriter = TestUserFactory.create("answerWriter");

        assertThatThrownBy(() -> answer.deleteAnswer(answerWriter, LocalDateTime.now()))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("답변을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("답변을 삭제한 시간과 삭제내역이 생성된 시간은 같아야 한다")
    void deleteAnswer_정상_삭제시간_일치() {
        final User user = TestUserFactory.create("writer");
        final Question question = TestQuestionFactory.create(user);
        final Answer answer = TestAnswerFactory.create(user, question);

        final LocalDateTime localDateTime = LocalDateTime.now();
        final DeleteHistory deleteHistory = 답변_삭제(answer, localDateTime);

        assertAll(
                () -> assertThat(deleteHistory).isNotNull(),
                () -> assertThat(deleteHistory.getCreateDate()).isEqualTo(localDateTime)
        );
    }

    private DeleteHistory 답변_삭제(Answer answer, LocalDateTime localDateTime) {
        return answer.deleteAnswer(answer.getWriter(), localDateTime);
    }
}
