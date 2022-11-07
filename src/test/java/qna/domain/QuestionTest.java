package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuestionTest {
    User writer;
    Question question;

    @BeforeEach
    void setUp() {
        UserAuth userAuth = new UserAuth("user", "password");
        writer = new User(userAuth, "name", "email@email.com");
        question = new Question("title", "contents").writeBy(writer);
    }

    @Test
    @DisplayName("질문의 작성자 지정")
    void write_by() {
        User expected = new User(new UserAuth("user2", "password"), "name2", "email2@email.com");
        Question actual = question.writeBy(expected);
        assertThat(actual.getWriter()).isEqualTo(expected);
    }

    @Test
    @DisplayName("질문에 답변을 추가")
    void add_answer() {
        Answer expected = new Answer(writer, question, "contents");
        question.addAnswer(expected);
        assertThat(question.getAnswers()).contains(expected);
    }

    @Test
    @DisplayName("질문의 주인일 경우 true를 리턴")
    void is_owner_return_true() {
        assertTrue(question.isOwner(writer));
    }
}
