package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.common.exception.InvalidParamException;
import qna.domain.qna.QuestionPost;

public class QuestionPostTest {

    public static final QuestionPost QUESTION_POST1 = QuestionPost.of("title1", "contents1");
    public static final QuestionPost QUESTION_POST2 = QuestionPost.of("title2", "contents2");

    @Test
    @DisplayName("정상 생성 후 제목,내용 검증")
    void create() {
        // given
        QuestionPost questionPost = QuestionPost.of("제목", "내용");

        // when
        // then
        assertAll(
            () -> assertThat(questionPost.getTitle()).isEqualTo("제목"),
            () -> assertThat(questionPost.getContents()).isEqualTo("내용")
        );
    }

    @Test
    @DisplayName("제목 빈값 에러")
    void create_title_실패() {
        assertThatThrownBy(() -> {
            // when
            QuestionPost.of("", "내용");
        })// then
            .isInstanceOf(InvalidParamException.class);
    }

    @Test
    @DisplayName("내용 빈값 에러")
    void create_contents_실패() {
        assertThatThrownBy(() -> {
            // when
            QuestionPost.of("제목", "");
        })// then
            .isInstanceOf(InvalidParamException.class);
    }

    @Test
    @DisplayName("제목 사이즈 에러")
    void create_title_over_size_실패() {
        assertThatThrownBy(() -> {
            // when
            QuestionPost.of(
                "제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목제목",
                "내용");
        })// then
            .isInstanceOf(InvalidParamException.class);
    }
}
