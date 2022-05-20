package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QuestionTest {
    User user;
    Question question;

    @BeforeEach
    void setUp() {
        user = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        question = new Question(1L, "title1", "contents1");
    }

    @Test
    void 답변_추가() {
        Question before = new Question(2L, "title2", "contents2");
        Answer answer = new Answer(1L, user, before, "Answers Contents2");
        question.addAnswer(answer);
        assertThat(question.getAnswers()).containsExactly(answer);
        assertThat(answer.getQuestion()).isSameAs(question);
    }
}
