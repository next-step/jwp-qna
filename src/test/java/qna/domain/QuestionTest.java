package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class QuestionTest {

    @Test
    @DisplayName("로그인한 사용자와 질문자가 같은 경우 답변이 없다면 삭제할 수 있고 삭제 시 삭제 이력이 남는다.")
    void deleteQuestion_정상_질문자_일치() throws CannotDeleteException {
        final Question question = 질문_생성("questionWriter");

        final List<DeleteHistory> deleteHistories = 질문_삭제(question);

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
    void deleteQuestion_예외_질문자_불일치() throws CannotDeleteException {
        final User anotherUser = 사용자_생성("anotherWriter");
        final Question question = 질문_생성("questionWriter");

        assertThatThrownBy(() -> question.deleteQuestion(anotherUser))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("질문자가 질문 삭제 시 모든 답변자가 질문자와 같다면 삭제할 수 있다")
    void deleteQuestion_정상_질문자_모든답변자_일치() {
        final Question question = 질문자_모든답변자_일치("questionWriter");

        final List<DeleteHistory> deleteHistories = 질문_삭제(question);

        assertAll(
                () -> assertThat(deleteHistories).hasSize(3),
                () -> assertThat(question.isDeleted()).isTrue()
        );

    }

    @Test
    @DisplayName("질문자가 질문 삭제 시 다른 사용자의 답변이 있다면 삭제할 수 없다.")
    void deleteQuestion_예외_질문자_다른답변자() {
        final Question question = 질문자_모든답변자_불일치("questionWriter");

        assertThatThrownBy(() -> question.deleteQuestion(question.getWriter()))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("답변을 삭제할 권한이 없습니다.");
    }

    private User 사용자_생성(String writer) {
        return new User(writer, "password", "lsh", "lsh@mail.com");
    }

    private Question 질문_생성(String writer) {
        return new Question("title", "contents", 사용자_생성(writer));
    }

    private List<DeleteHistory> 질문_삭제(Question question) {
        return question.deleteQuestion(question.getWriter());
    }

    private Answer 답변_생성(String writer) {
        return new Answer(사용자_생성(writer), 질문_생성(writer), "contents");
    }

    private Question 질문자_모든답변자_일치(String writer) {
        final Question question = 질문_생성(writer);
        final Answers answers = new Answers();
        answers.addAnswer(답변_생성(writer));
        answers.addAnswer(답변_생성(writer));
        question.setAnswers(answers);
        return question;
    }

    private Question 질문자_모든답변자_불일치(String writer) {
        final Question question = 질문_생성(writer);
        final Answers answers = new Answers();
        answers.addAnswer(답변_생성(writer));
        answers.addAnswer(답변_생성("anotherWriter"));
        question.setAnswers(answers);
        return question;
    }
}
