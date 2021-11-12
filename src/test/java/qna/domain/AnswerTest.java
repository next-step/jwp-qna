package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AnswerTest {
    @Test
    @DisplayName("답글의 질문을 바꾸는 경우, 질문에서도 답글이 바뀐다")
    void changeQuestion() {
        Question question = new Question(2L, "title", "contents");
        Answer answer = new Answer(1L, new User(), question, null);

        answer.setQuestion(question);

        assertAll(
            () -> assertThat(answer.getQuestion().getId()).isEqualTo(question.getId()),
            () -> assertThat(question.getAnswers().size()).isEqualTo(1),
            () -> assertThat(question.getAnswers().get(0).getId()).isEqualTo(answer.getId())
        );
    }
}
