package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

@DataJpaTest
public class AnswersTest {

    @Test
    @DisplayName("질문 추가 테스트")
    void addQuestionTest() {
        Answers answers = new Answers();
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        answers.addAnswer(answer);
        assertThat(answers.getAnswers()).contains(answer);
    }

    @Test
    @DisplayName("모든 답변 삭제 테스트")
    void allAnswersDeleteTest() {
        Answers answers = new Answers();
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        answers.addAnswer(answer);
        answers.delete();
        assertThat(answer.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("다른유저 답변삭제시 에러 테스트")
    void otherUserDeleteTest() {
        Answers answers = new Answers();
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        answers.addAnswer(answer);
        assertThatThrownBy(() -> answers.validateAnswersWriter(UserTest.SANJIGI))
            .isInstanceOf(CannotDeleteException.class)
            .hasMessage("다른 사람이 쓴 답변이 있는 경우 삭제할 수 없습니다");
    }
}
