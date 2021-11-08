package qna.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void delete_답변을_삭제한다() {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        A1.delete(deleteHistories);
        assertAll(
                () -> assertThat(deleteHistories.size()).isEqualTo(1),
                () -> assertThat(A1.isDeleted()).isTrue()
        );
    }
}
