package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AnswersTest {
    private User writer = new User("writer", "password", "name", "email");

    @Test
    void Answers에_Answer가_추가되면_Answer와_Question_간의_연관관계가_올바르게_설정되어야_한다() {
        // given
        final Question originQuestion = new Question(1L, "origin title", "origin contents");
        final Answer answer = new Answer(writer, originQuestion, "answer");

        final Question newQuestion = new Question(2L, "new title", "new contents");
        final Answers answers = newQuestion.getAnswers();

        // when
        answers.add(answer, newQuestion);

        // then
        assertThat(answer.getQuestion()).isEqualTo(newQuestion);
    }
}
