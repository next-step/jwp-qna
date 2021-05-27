package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("answer 정상 생성 테스트")
    void create() {
        Question question = new Question(1L, "질문1", "질문내용");
        Answer answer = new Answer(UserTest.JAVAJIGI, question, "답변내용");

        assertThat(answer).isNotNull();
        assertThat(answer).isEqualTo(new Answer(UserTest.JAVAJIGI, question, "답변내용"));
    }

    @Test
    @DisplayName("answer 작성자명 null 일때 UnAuthorizedException 예외 발생")
    void validateWriter() {
        Question question = new Question(1L, "질문1", "질문내용");

        assertThatThrownBy(() -> new Answer(1L, null, question, "답변내용"))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("answer 질문 null 일때 NotFoundException 예외 발생")
    void validateQuestion() {
        Question question = null;

        assertThatThrownBy(() -> new Answer(1L, UserTest.SANJIGI, question, "답변내용"))
                .isInstanceOf(NotFoundException.class);
    }
}
