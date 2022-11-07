package qna.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnswerTest {
    Question question;
    Answer answer;
    User writer;

    @BeforeEach
    void setUp() {
        writer = new User("user", "password", "name", "email@email.com");
        question = new Question("title", "contents").writeBy(writer);
        answer = new Answer(question.getWriter(), question, "contents");
    }

    @Test
    @DisplayName("답변의 주인이라면 true를 리턴")
    void is_owner_return_true() {
        assertTrue(answer.isOwner(writer));
    }
    
    @Test
    @DisplayName("질문 변경")
    void to_question() {
        Question expected = new Question("title2", "contents2").writeBy(writer);
        answer.toQuestion(expected);
        assertEquals(expected, answer.getQuestion());
    }
}
