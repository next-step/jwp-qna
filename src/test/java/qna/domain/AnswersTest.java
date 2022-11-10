package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswersTest {

    @Test
    @DisplayName("생성자에서 null값 입력시 예외발생")
    public void test_throw_exception() {
        assertThatThrownBy(() -> new Answers(null))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("질문목록이 없습니다");
    }

    @Test
    @DisplayName("답변추가하면 추가한만큼 개수반환")
    public void test_returns_count_when_add_answer() {
        Answers answers = new Answers();

        answers.addAnswer(new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1"));
        answers.addAnswer(new Answer(UserTest.SANJIGI, QuestionTest.Q2, "Answers Contents2"));

        assertThat(answers.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("다른 사람이 쓴 답변이 있으면 예외발생 ")
    public void test_throw_exception_when_other_writer() {
        Answers answers = new Answers();

        answers.addAnswer(new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1"));
        answers.addAnswer(new Answer(UserTest.SANJIGI, QuestionTest.Q2, "Answers Contents2"));

        assertThatThrownBy(() -> answers.deleteAllAnswer(UserTest.SANJIGI))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

}
