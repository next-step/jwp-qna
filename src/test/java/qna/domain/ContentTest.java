package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ContentTest {
    @DisplayName("질문 콘텐츠 생성")
    @Test
    void constructQuestionContent() {
        User user = new User(1L, "userId", "password", "name", "email");
        Question question = new Question(1L, "title", "contents").writeBy(user);

        assertThat(new Content(question)).isEqualTo(new Content(ContentType.QUESTION, 1L, user));
    }

    @DisplayName("답변 콘텐츠 생성")
    @Test
    void constructAnswerContent() {
        User user = new User(1L, "userId", "password", "name", "email");
        Question question = new Question(1L, "title", "contents").writeBy(user);
        Answer answer = new Answer(1L, user, question, "answer");

        assertThat(new Content(answer)).isEqualTo(new Content(ContentType.ANSWER, 1L, user));
    }
}