package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.UserTest.JAVAJIGI;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoriesTest {
    public static final Question Q1 = Question.of("title1", "contents1").writeBy(1L);
    private static final Answer A1 = new Answer(1L, QuestionTest.Q1, "Answers Contents1");
    private static final Answer A2 = new Answer(2L, QuestionTest.Q1, "Answers Contents2");

    @Test
    void 생성() {
        assertThat(new DeleteHistories()).isEqualTo(new DeleteHistories());
    }

    @Test
    void DeleteHistory_Question_추가() {
        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.addQuestionDeleteHistory(Q1, JAVAJIGI);
        assertThat(deleteHistories.size()).isEqualTo(1);
    }

    @Test
    void DeleteHistory_Answers_추가() {
        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.addAnswersDeleteHistory(new Answers(new ArrayList<>(Arrays.asList(A1, A2))), JAVAJIGI);
        assertThat(deleteHistories.size()).isEqualTo(2);
    }
}
