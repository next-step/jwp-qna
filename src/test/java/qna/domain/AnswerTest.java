package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AnswerTest {
    User user;
    Question question;
    Answer answer;

    @BeforeEach
    void setUp() {
        user = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        question = new Question(1L, "title1", "contents1");
        answer = new Answer(1L, user, question, "Answers Contents2");
    }

    @Test
    void 답변_추가() {
        assertThat(answer.getQuestion()).isSameAs(question);
        assertThat(question.getAnswers()).containsExactly(answer);
    }
}
