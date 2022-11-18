package qna.domain;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswersTest {

    private Answers answers;
    private Answer answer;

    @BeforeEach
    void init() {
        answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        answers = new Answers();
    }

    @Test
    @DisplayName("Answers 추가 후 삭제")
    void Answers_add_same_answer() {
        answers.add(answer);
        DeleteHistories deleteHistories = answers.deleteAnswers();

        assertThat(deleteHistories.getDeleteHistories()).hasSize(1);
    }

    @Test
    @DisplayName("Answers writer 와 loginUser check 확인")
    void Answers_writer_check_loginUser() {
        answers.add(answer);

        assertThat(answers.checkWriterAndLoginUser(UserTest.JAVAJIGI)).isFalse();
        assertThat(answers.checkWriterAndLoginUser(UserTest.SANJIGI)).isTrue();
    }
}
