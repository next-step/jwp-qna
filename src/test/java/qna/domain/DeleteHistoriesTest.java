package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DeleteHistoriesTest {

    private Question question;

    @BeforeEach
    void setup() {
        question = new Question(1L, "title", "contents").writeBy(UserTest.JAVAJIGI);
    }

    @DisplayName("퀘스트 삭제 목록 생성되는지 확인")
    @Test
    void ofQuestion() {
        DeleteHistories deleteHistories = DeleteHistories.ofQuestion(question);

        assertThat(deleteHistories.getDeleteHistories())
            .containsExactly(new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now()));
    }

    @DisplayName("삭제 목록 합쳐지는지 확인")
    @Test
    void addAll() {
        DeleteHistory deleteHistory1 = new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI, LocalDateTime.now());
        DeleteHistory deleteHistory2 = new DeleteHistory(ContentType.ANSWER, 2L, UserTest.JAVAJIGI, LocalDateTime.now());

        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.addAll(Arrays.asList(deleteHistory1, deleteHistory2));

        assertThat(deleteHistories.getDeleteHistories()).contains(deleteHistory1, deleteHistory2);
    }
}