package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.domain.fixture.TestAnswerFactory;
import qna.domain.fixture.TestAnswersFactory;
import qna.domain.fixture.TestQuestionFactory;
import qna.domain.fixture.TestUserFactory;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class QuestionTest {

    @Test
    @DisplayName("로그인한 사용자와 질문자가 같은 경우 답변이 없다면 삭제할 수 있고 삭제 시 삭제 이력이 남는다.")
    void deleteQuestion_정상_로그인사용자_질문자_일치() throws CannotDeleteException {
        final User user = TestUserFactory.create("writer");
        final Question question = TestQuestionFactory.create(user);

        final List<DeleteHistory> deleteHistories = 질문_삭제(question, LocalDateTime.now());

        assertAll(
                () -> assertThat(question.isDeleted()).isTrue(),
                () -> assertThat(deleteHistories).hasSize(1),
                () -> assertThat(deleteHistories.get(0).getDeletedBy()).isEqualTo(question.getWriter()),
                () -> assertThat(deleteHistories.get(0).getCreateDate()).isNotNull(),
                () -> assertThat(deleteHistories.get(0).getContentType()).isEqualTo(ContentType.QUESTION)
        );
    }

    @Test
    @DisplayName("로그인한 사용자와 질문자가 다른 경우 질문을 삭제할 수 없다.")
    void deleteQuestion_예외_로그인사용자_질문자_불일치() throws CannotDeleteException {
        final User questionWriter = TestUserFactory.create("questionWriter");
        final Question question = TestQuestionFactory.create(questionWriter);
        final User answerWriter = TestUserFactory.create("answerWriter");

        assertThatThrownBy(() -> question.deleteQuestion(answerWriter, LocalDateTime.now()))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("질문자가 질문 삭제 시 모든 답변자가 질문자와 같다면 삭제할 수 있다")
    void deleteQuestion_정상_질문자_모든답변자_일치() {

        final User user = TestUserFactory.create("writer");
        final Question question = TestQuestionFactory.create(user);
        final Answer answer1 = TestAnswerFactory.create(user, question);
        final Answers answers = TestAnswersFactory.create(answer1);

        final List<DeleteHistory> deleteHistories = 질문_삭제(question, LocalDateTime.now());

        assertAll(
                () -> assertThat(deleteHistories).hasSize(2),
                () -> assertThat(question.isDeleted()).isTrue(),
                () -> assertThat(answer1.isDeleted()).isTrue(),
                () -> assertThat(answers.getAnswers()).hasSize(1)
        );
    }

    @Test
    @DisplayName("질문자가 질문 삭제 시 다른 사용자의 답변이 있다면 삭제할 수 없다.")
    void deleteQuestion_예외_질문자_다른답변자_불일치() {

        final User user = TestUserFactory.create("writer");
        final Question question = TestQuestionFactory.create(user);
        final User answerUser = TestUserFactory.create("answerUser");
        final Answer answer1 = TestAnswerFactory.create(answerUser, question);
        final Answers answers = TestAnswersFactory.create(answer1);

        assertThatThrownBy(() -> question.deleteQuestion(user, LocalDateTime.now()))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("답변을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("질문을 삭제한 시간과 삭제내역이 생성된 시간은 같아야 한다")
    void deleteQuestion_정상_삭제시간_일치() {
        final User user = TestUserFactory.create("writer");
        final Question question = TestQuestionFactory.create(user);

        final LocalDateTime localDateTime = LocalDateTime.now();
        final List<DeleteHistory> deleteHistories = 질문_삭제(question, localDateTime);

        assertAll(
                () -> assertThat(deleteHistories).hasSize(1),
                () -> assertThat(deleteHistories.get(0).getCreateDate()).isEqualTo(localDateTime)
        );
    }

    private List<DeleteHistory> 질문_삭제(final Question question, final LocalDateTime localDateTime) {
        return question.deleteQuestion(question.getWriter(), localDateTime);
    }
}
