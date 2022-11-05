package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("질문 작성자 확인 테스트")
    void owner() {
        Question question = new Question(1L, "title", "contents").writeBy(UserTest.JAVAJIGI);
        Assertions.assertThat(question.isOwner(UserTest.JAVAJIGI)).isTrue();
    }

    @Test
    @DisplayName("삭제 여부 true 테스트")
    void delete() {
        Question question = new Question(1L, "title", "contents");
        question.deleted();
        Assertions.assertThat(question.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("삭제 여부 false 테스트")
    void delete2() {
        Question question = new Question(1L, "title", "contents");
        Assertions.assertThat(question.isDeleted()).isFalse();
    }
}
