package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qna.domain.exception.IllegalDeleteHistoriesException;
import qna.domain.exception.IllegalDeleteHistoryException;

class DeleteHistoriesTest {

    private Question question;
    private Answer answer1;
    private Answer answer2;

    private List<DeleteHistory> deleteHistories;

    @BeforeEach
    void setUp() {
        question = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
        answer1 = new Answer(1L, UserTest.JAVAJIGI, question, "Answers Contents1");
        answer2 = new Answer(2L, UserTest.JAVAJIGI, question, "Answers Contents2");
        question.answers.addAnswer(answer1);
        question.answers.addAnswer(answer2);

        deleteHistories = Arrays.asList(
            new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter()
            ),
            new DeleteHistory(ContentType.ANSWER, answer1.getId(), answer1.getWriter()
            )
        );
    }

    @Test
    void test_delete_history_리스트로_생성() {
        DeleteHistories histories = DeleteHistories.from(deleteHistories);

        assertThat(histories).isNotNull();
    }

    @Test
    void test_delete_history_와_histories_로_생성() {
        DeleteHistory questionDeleteHistory = new DeleteHistory(ContentType.QUESTION,
            question.getId(), question.getWriter());

        DeleteHistories answerDeleteHistories = DeleteHistories.from(
            Arrays.asList(
                new DeleteHistory(ContentType.ANSWER, answer1.getId(), answer1.getWriter()),
                new DeleteHistory(ContentType.ANSWER, answer2.getId(), answer2.getWriter())
            )
        );

        DeleteHistories histories = DeleteHistories.of(questionDeleteHistory,
            answerDeleteHistories);

        assertAll(
            () -> assertThat(histories).isNotNull(),
            () -> assertThat(histories.size()).isEqualTo(3)
        );
    }

    @Test
    void test_delete_history가_null이면_예외() {
        assertThatThrownBy(() -> DeleteHistories.of(null, DeleteHistories.from(deleteHistories)))
            .isInstanceOf(IllegalDeleteHistoryException.class);
    }

    @Test
    void test_delete_histories가_null이면_예외() {
        assertThatThrownBy(() -> DeleteHistories.from(null))
            .isInstanceOf(IllegalDeleteHistoriesException.class);
    }

    @Test
    void test_동일한_history_리스트_반환() {
        DeleteHistories histories = DeleteHistories.from(deleteHistories);

        assertAll(
            () -> assertThat(histories).isNotNull(),
            () -> assertThat(histories.deleteHistories()).isEqualTo(deleteHistories)
        );
    }

    @Test
    void test_size() {
        DeleteHistories histories = DeleteHistories.from(deleteHistories);

        assertAll(
            () -> assertThat(histories).isNotNull(),
            () -> assertThat(histories.deleteHistories()).hasSize(2),
            () -> assertThat(histories.size()).isEqualTo(2)
        );
    }
}