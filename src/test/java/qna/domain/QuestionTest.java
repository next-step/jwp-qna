package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("생성 테스트")
    void create() {
        // given
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        // when & then
        assertAll(
            () -> assertThat(question.getTitle()).isEqualTo(Q1.getTitle()),
            () -> assertThat(question.getContents()).isEqualTo(Q1.getContents()),
            () -> assertThat(question.getWriterId()).isEqualTo(Q1.getWriterId())
        );
    }

    @Test
    @DisplayName("작성자 비교 테스트")
    void isOwner() {
        // given & when & then
        assertThat(Q1.isOwner(UserTest.JAVAJIGI)).isTrue();
        assertThat(Q1.isOwner(UserTest.SANJIGI)).isFalse();
    }
}
