package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @DisplayName("답변 삭제 할때 삭제 상태로 변경해준다.")
    @Test
    void deleteTest() {
        A1.delete();

        assertThat(A1.isDeleted()).isTrue();
    }

    @DisplayName("답변 삭제 시 DeleteHistory에 이력정보객체 생성.")
    @Test
    void deleteHistoryToAnswer() {
        DeleteHistory deleteHistory = A1.delete();

        assertThat(deleteHistory).isNotNull();
        assertThat(deleteHistory).isEqualTo(new DeleteHistory(ContentType.ANSWER, A1.getId(), UserTest.JAVAJIGI));
    }
}
