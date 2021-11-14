package qna.deletehistory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.answer.AnswerTest.A1;
import static qna.answer.AnswerTest.A2;
import static qna.domain.QuestionTest.Q1;

class DeleteHistoriesTest {

    private static DeleteHistories deleteHistories;

    @BeforeAll
    public static void beforeAll() {
        deleteHistories = DeleteHistories.fromAnswers(Arrays.asList(A1, A2));
    }

    @Test
    @DisplayName("삭제 이력 리스트 객체 생성")
    public void createDeleteHistoriesTest() throws Exception {
        assertThat(deleteHistories).isEqualTo(DeleteHistories.fromAnswers(Arrays.asList(A1, A2)));
    }

    @Test
    @DisplayName("삭제된 질문 추가")
    public void addDeleteQuestionTest() {
        deleteHistories.addDeleteQuestion(Q1);
        assertThat(deleteHistories.getDeleteHistories()).hasSize(3);
    }

}