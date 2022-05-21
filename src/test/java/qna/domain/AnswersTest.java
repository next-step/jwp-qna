package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AnswersTest {
    User loginUser;
    Question question;
    Answer answer1;
    Answer answer2;
    Answers answers;

    @BeforeEach
    void setUp() {
        loginUser = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        question = new Question(1L, "title1", "contents1");
        answer1 = new Answer(1L, loginUser, question, "Answers Contents1");
        answer2 = new Answer(2L, loginUser, question, "Answers Contents2");
        answers = new Answers(Arrays.asList(answer1, answer2));
    }

    @Test
    void 답변_삭제() {
        answers.deleteBy(loginUser);
        assertThat(answer1.isDeleted()).isTrue();
        assertThat(answer2.isDeleted()).isTrue();
    }

    @Test
    void 답변_삭제_이력() {
        List<DeleteHistory> deleteHistories = answers.deleteBy(loginUser);
        assertThat(deleteHistories).containsOnly(
                new DeleteHistory(ContentType.ANSWER, answer1.getId(), answer1.getWriter()),
                new DeleteHistory(ContentType.ANSWER, answer2.getId(), answer2.getWriter())
        );
    }
}
