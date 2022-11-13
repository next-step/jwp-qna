package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class AnswersTest {
    public static final Answer A1 = new Answer(1L, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(2L, QuestionTest.Q1, "Answers Contents2");

    @Test
    void 생성() {
        assertThat(new Answers(Arrays.asList(A1, A2))).isEqualTo(new Answers(Arrays.asList(A1, A2)));
    }

    @Test
    void DeleteHistories에_DeleteHistory_추가() {
        Answers answers = new Answers(new ArrayList<>(Arrays.asList(A1, A2)));
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        answers.addDeleteHistory(deleteHistories, UserTest.JAVAJIGI);
        assertThat(deleteHistories.size()).isEqualTo(2);
    }

    @Test
    void Answer_추가() {
        Answers answers = new Answers();
        Answer answer = new Answer();
        answers.addAnswer(answer);
        assertThat(answers.size()).isEqualTo(1);
    }
}
