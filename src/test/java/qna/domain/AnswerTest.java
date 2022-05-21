package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnswerTest {
    User loginUser;
    Question question;
    Answer answer;

    @BeforeEach
    void setUp() {
        loginUser = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        question = new Question(1L, "title1", "contents1");
        answer = new Answer(1L, loginUser, question, "Answers Contents2");
    }

    @Test
    void 답변_추가() {
        assertThat(answer.getQuestion()).isSameAs(question);
        assertThat(question.getAnswers()).containsExactly(answer);
    }

    @Test
    void 답변_삭제() throws CannotDeleteException {
        answer.deleteBy(loginUser);
        assertThat(answer.isDeleted()).isTrue();
    }

    @Test
    void 다른_사람이_쓴_글_삭제_불가() {
        User otherUser = new User(2L, "geunhwanlee", "password", "gunan", "gunan@gmail.com");
        Answer otherAnswer = new Answer(2L, otherUser, question, "Answers Contents5");
        assertThatThrownBy(() -> otherAnswer.deleteBy(loginUser))
                .isInstanceOf(CannotDeleteException.class);
    }
}
