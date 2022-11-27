package qna.domain;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswersTest {
    @Test
    void 질문_추가() {
        Answers answers = new Answers();
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        answers.add(answer);
        assertThat(answers.getAnswers()).contains(answer);
    }

    @Test
    void 모든_답변_삭제() {
        Answers answers = new Answers();
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        answers.add(answer);
        answers.deleteAll(UserTest.JAVAJIGI);
        assertThat(answer.isDeleted()).isTrue();
    }

    @Test
    void 다른유저_답변삭제시_에러() {
        Answers answers = new Answers();
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        answers.add(answer);
        assertThatThrownBy(() -> answers.deleteAll(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
