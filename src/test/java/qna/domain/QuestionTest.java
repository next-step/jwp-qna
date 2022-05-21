package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QuestionTest {
    User loginUser;
    Question question;

    @BeforeEach
    void setUp() {
        loginUser = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        question = new Question(1L, "title1", "contents1");
    }

    @Test
    void 답변_추가() {
        Question before = new Question(2L, "title2", "contents2");
        Answer answer = new Answer(1L, loginUser, before, "Answers Contents2");
        question.addAnswer(answer);
        assertThat(question.getAnswers()).containsExactly(answer);
        assertThat(answer.getQuestion()).isSameAs(question);
    }

    @Test
    void 질문_삭제() {
        question.writeBy(loginUser);
        question.deleteWithAnswersBy(loginUser);
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    void 다른_사람이_쓴_글_삭제_불가() {
        User otherUser = new User(2L, "geunhwanlee", "password", "gunan", "gunan@gmail.com");
        question.writeBy(otherUser);
        assertThatThrownBy(() -> question.deleteWithAnswersBy(loginUser))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void 다른_사람이_쓴_답변_존재하면_삭제_불가() {
        User otherUser = new User(2L, "geunhwanlee", "password", "gunan", "gunan@gmail.com");
        question.writeBy(loginUser);
        new Answer(1L, otherUser, question, "Answers Contents2");
        assertThatThrownBy(() -> question.deleteWithAnswersBy(loginUser))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void 질문_삭제_이력() {
        question.writeBy(loginUser);
        Answer answer = new Answer(1L, loginUser, question, "Answers Contents2");
        List<DeleteHistory> deleteHistories = question.deleteWithAnswersBy(loginUser);
        assertThat(deleteHistories).containsExactly(
                new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter()),
                new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter())
        );
    }
}
