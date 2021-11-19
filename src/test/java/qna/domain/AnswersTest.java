package qna.domain;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class AnswersTest {

    @Test
    @DisplayName("질문자가 질문 삭제 모든 답변자가 질문자와 같다면 삭제할 수 있다.")
    void deleteAnswer_정상_질문자_모든답변자_동일() {
        final User questionWriter = 질문자_생성("questionWriter");
        final Answers answers = 모든답변자_동일("questionWriter");

        List<DeleteHistory> deleteHistories = 답변_삭제(answers, questionWriter);

        assertAll(
                () -> assertThat(deleteHistories).hasSize(2),
                () -> assertThat(deleteHistories.get(0).getDeletedBy()).isEqualTo(questionWriter),
                () -> assertThat(deleteHistories.get(1).getDeletedBy()).isEqualTo(questionWriter),
                () -> assertThat(deleteHistories.get(0).getContentType()).isEqualTo(ContentType.ANSWER),
                () -> assertThat(deleteHistories.get(1).getContentType()).isEqualTo(ContentType.ANSWER)
        );
    }

    @Test
    @DisplayName("질문자가 질문 삭제 시 질문자가 아닌 다른 사용자의 답변이 있다면 삭제할 수 없다.")
    void deleteAnswer_예외_질문자_모든답변자_불일치() {
        final User questionWriter = 질문자_생성("questionWriter");
        final Answers answers = 모든답변자_불일치();

        assertThatThrownBy(() -> answers.deleteAnswers(questionWriter))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("답변을 삭제할 권한이 없습니다.");
    }

    private User 질문자_생성(String writer) {
         return new User(writer, "password", "lsh", "lsh@mail.com");
    }

    private Question 질문_생성() {
        return new Question("title", "contents", 질문자_생성("questionWriter"));
    }

    private Answers 모든답변자_동일(String writer) {
        final Answers answers = new Answers();
        final Answer answer = new Answer(질문자_생성(writer), 질문_생성(), "contents");
        answers.addAnswer(answer);
        answers.addAnswer(answer);
        return answers;
    }

    private Answers 모든답변자_불일치() {
        final Answers answers = new Answers();
        final Answer answer1 = new Answer(질문자_생성("questionWriter"), 질문_생성(), "contents");
        final Answer answer2 = new Answer(질문자_생성("answerWriter"), 질문_생성(), "contents");
        answers.addAnswer(answer1);
        answers.addAnswer(answer2);
        return answers;
    }

    private List<DeleteHistory> 답변_삭제(Answers answers, User questionWriter) {
        return answers.deleteAnswers(questionWriter);
    }
}
