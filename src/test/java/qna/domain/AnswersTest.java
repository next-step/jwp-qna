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

class AnswersTest {

    @Test
    @DisplayName("질문자가 질문 삭제 모든 답변자가 질문자와 같다면 삭제할 수 있다.")
    void deleteAnswer_정상_질문자_모든답변자_동일() {
        final User user = TestUserFactory.create("writer");
        final Question question = TestQuestionFactory.create(user);
        final Answer answer = TestAnswerFactory.create(user, question);
        final Answers answers = TestAnswersFactory.create(answer);

        List<DeleteHistory> deleteHistories = 답변_삭제(answers, user);

        assertAll(
                () -> assertThat(deleteHistories).hasSize(1),
                () -> assertThat(deleteHistories.get(0).getDeletedBy()).isEqualTo(user),
                () -> assertThat(deleteHistories.get(0).getContentType()).isEqualTo(ContentType.ANSWER)
        );
    }

    @Test
    @DisplayName("질문자가 질문 삭제 시 질문자가 아닌 다른 사용자의 답변이 있다면 삭제할 수 없다.")
    void deleteAnswer_예외_질문자_모든답변자_불일치() {
        final User user = TestUserFactory.create("writer");
        final Question question = TestQuestionFactory.create(user);
        final User anotherUser = TestUserFactory.create("anotherUser");
        final Answer answer1 = TestAnswerFactory.create(user, question);
        final Answer answer2 = TestAnswerFactory.create(anotherUser, question);
        final Answers answers = new Answers();
        answers.addAnswer(answer1);
        answers.addAnswer(answer2);

        assertThatThrownBy(() -> answers.deleteAnswers(user, LocalDateTime.now()))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("답변을 삭제할 권한이 없습니다.");
    }

    private List<DeleteHistory> 답변_삭제(Answers answers, User questionWriter) {
        return answers.deleteAnswers(questionWriter, LocalDateTime.now());
    }
}
