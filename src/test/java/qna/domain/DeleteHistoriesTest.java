package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DeleteHistoriesTest {
    
    @Test
    @DisplayName("삭제이력 목록 생성 확인")
    void 삭제이력_목록_확인() {
        List<DeleteHistory> answerHistories = new ArrayList<DeleteHistory>();
        answerHistories.add(DeleteHistory.of(ContentType.ANSWER, AnswerTest.A2.getId(), UserTest.JENNIE));
        assertThat(DeleteHistories.of(DeleteHistory.of(ContentType.QUESTION, QuestionTest.Q1.getId(), UserTest.JENNIE), DeleteHistories.fromAnswers(answerHistories)).getDeleteHistories()).hasSize(2);
    }

}
