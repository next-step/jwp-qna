package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.message.QuestionMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("질문을 생성한다")
    void create_question_test() {
        Question question = new Question("title1", "Contents");
        assertThat(question).isEqualTo(new Question("title1", "Contents"));
    }

    @Test
    @DisplayName("질문 생성시 제목이 누락되면 [IllegalArgumentException] 예외처리한다")
    void create_question_without_title_test() {
        assertThatThrownBy(() -> new Question(null, "Contents"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(QuestionMessage.ERROR_TITLE_SHOULD_BE_NOT_NULL.message());
    }

    @Test
    @DisplayName("질문 생성시 내용이 누락되면 [IllegalArgumentException] 예외처리한다")
    void create_question_without_contents_test() {
        assertThatThrownBy(() -> new Question("title", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(QuestionMessage.ERROR_CONTENTS_SHOULD_BE_NOT_NULL.message());
    }

    @Test
    @DisplayName("질문을 삭제하면 논리삭제를 진행한다.")
    void delete_question_test() {
        Question question = new Question("title1", "Contents");
        question.delete();
        assertThat(question.isDeleted()).isTrue();
    }
}
