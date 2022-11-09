package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

class AnswersTest {
    @Test
    void 질문_추가() {
        Answers answers = new Answers();
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        answers.add(answer);
        assertThat(answers.getAnswers()).contains(answer);
    }

    @Test
    void 질문_여러개를_삭제하고_삭제_이력을_반환_받을_수_있다() throws CannotDeleteException {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        Answer anotherAnswer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        Answers answers = new Answers();
        answers.add(answer);
        answers.add(anotherAnswer);

        List<DeleteHistory> deleteHistories = answers.deleteAll(UserTest.JAVAJIGI);

        assertThat(answer.isDeleted()).isTrue();
        assertThat(anotherAnswer.isDeleted()).isTrue();
        assertThat(deleteHistories).hasSize(2);
    }
}
