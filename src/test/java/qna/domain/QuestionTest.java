package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("질문 삭제 할 때 삭제 상태로 변경 검증")
    @Test
    void deleteTest() {
        Q1.delete();
        assertThat(Q1.isDeleted()).isTrue();

    }

    @DisplayName("질문 삭제 시 DeleteHistory에 이력정보 객체 생성 검증")
    @Test
    void deleteHistoryToQuestion() {
        DeleteHistory deleteHistory = Q1.delete();

        assertThat(deleteHistory).isNotNull();
        assertThat(deleteHistory).isEqualTo(new DeleteHistory(ContentType.QUESTION, Q1.getId(), UserTest.JAVAJIGI));

    }
}
