package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

public class DeleteHistoriesTest {
    @Test
    @DisplayName("DeleteHistories 생성")
    void DeleteHistories_생성(){
        DeleteHistories deleteHistories = DeleteHistories.of(
                QuestionTest.Q1,
                new Answers(Arrays.asList(AnswerTest.A1, AnswerTest.A2)),
                UserTest.JAVAJIGI
        );
        assertThat(deleteHistories.getDeleteHistories().size()).isEqualTo(3);
    }
}
