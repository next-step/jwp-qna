package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class QuestionTest {
    public static final Question Q1 = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question(2L, "title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("Question 객체 기본 API 테스트")
    @Test
    void questionNormal() {
        assertAll(
                () -> assertThat(Q1.isOwner(UserTest.JAVAJIGI)).isTrue(),
                () -> assertThat(Q1.isOwner(UserTest.SANJIGI)).isFalse()
        );
    }

    @DisplayName("Question 객체 deleted 변경 테스트")
    @Test
    void deleteQuestion() {
        Question question = new Question("title3", "contents3");
        question.delete();

        assertThat(question.isDeleted()).isTrue();
    }
}
