package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {

    private Question question;

    @BeforeEach
    void setUp() {
        User javajigi = new User("javajigi", "1234", "javajigi", "a@email.com");
        question = new Question("title1", "contents1").writeBy(javajigi);
    }

    @Test
    void 질문_삭제_권한_없을_때() {
        User sanjigi = new User("sanjigi", "1234", "javajigi", "a@email.com");
        assertThatThrownBy(() -> question.validateQuestionOwner(sanjigi, question)).isInstanceOf(CannotDeleteException.class);
    }
}
