package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void 답변_작성자가_로그인_사용와_같은지_비교() {
        Answer answer = AnswerTest.A1;

        User user = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        User other = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

        assertThat(answer.isWriterUser(user)).isTrue();
        assertThat(answer.isWriterUser(other)).isFalse();

    }
}
