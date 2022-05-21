package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AnswersTest {
    User loginUser;
    Question question;
    Answers answers;

    @BeforeEach
    void setUp() {
        loginUser = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        question = new Question(1L, "title1", "contents1");
        answers = new Answers(Arrays.asList(
                new Answer(1L, loginUser, question, "Answers Contents1"),
                new Answer(2L, loginUser, question, "Answers Contents2")
        ));
    }

    @Test
    void 답변_삭제() {
        answers.deleteBy(loginUser);
        answers.getAnswers().forEach(answer -> assertThat(answer.isDeleted()).isTrue());
    }

    @Test
    void 답변_삭제_이력() {
        List<DeleteHistory> deleteHistories = answers.deleteBy(loginUser);
        assertThat(deleteHistories).containsExactly(
                new DeleteHistory(ContentType.ANSWER, 1L, loginUser),
                new DeleteHistory(ContentType.ANSWER, 2L, loginUser)
        );
    }
}
