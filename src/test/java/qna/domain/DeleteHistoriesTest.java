package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteHistoriesTest {

    private Question question;

    @BeforeEach
    void setup() {
        question = new Question(1L, "title", "contents").writeBy(UserTest.JAVAJIGI);
    }

    @Test
    void ofQuestion_성공() {
        DeleteHistories deleteHistories = DeleteHistories.ofQuestion(question);

        assertThat(deleteHistories.getDeleteHistories())
                .containsExactly(new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter()));
    }

    @Test
    void addAll_성공() {
        DeleteHistory deleteHistory1 = new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI);
        DeleteHistory deleteHistory2 = new DeleteHistory(ContentType.ANSWER, 2L, UserTest.JAVAJIGI);

        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.addAll(Arrays.asList(deleteHistory1, deleteHistory2));

        assertThat(deleteHistories.getDeleteHistories()).contains(deleteHistory1, deleteHistory2);
    }
}