package qna.domain;

import org.junit.jupiter.api.Test;

class AnswerTest {
    private User user1 = new User("javajigi", "password", "name", "javajigi@slipp.net");

    @Test
    void canCrate() {
        Question question1 = new Question(1L, "title", "content");
        new Answer(user1, question1, "content");
    }
}
