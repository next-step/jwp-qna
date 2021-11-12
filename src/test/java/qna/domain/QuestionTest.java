package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class QuestionTest {

    @Test
    @DisplayName("질문에 답글을 추가하는 경우, 답글의 질문도 바뀐다")
    void addAnswer() {
        Question question = new Question(2L, "title", "contents");
        Answer answer = new Answer(1L, new User(), question, null);

        question.addAnswer(answer);

        assertAll(
            () -> assertThat(answer.getQuestion().getId()).isEqualTo(question.getId()),
            () -> assertThat(question.getAnswers().size()).isEqualTo(1),
            () -> assertThat(question.getAnswers().get(0).getId()).isEqualTo(answer.getId())
        );
    }
}
