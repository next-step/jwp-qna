package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class QuestionTitleTest {
    @DisplayName("save questionTitle")
    @Test
    void getQuestionTitle() {
        // given, when
        QuestionTitle questionTitle = new QuestionTitle("question title");

        // then
        assertThat(questionTitle.getTitle()).isEqualTo("question title");
    }

    @DisplayName("get questionTitle Max Length 예외")
    @Test
    void getQuestionTitleMaxLengthException() {
        assertThatThrownBy(() -> {
            // when
            QuestionTitle questionTitle = new QuestionTitle("question title 100 글자 넘기기 100 글자 넘기기 100 글자 넘기기 " +
                    "100 글자 넘기기 100 글자 넘기기 100 글자 넘기기 100 글자 넘기기 100 글자 넘기기 100 글자 넘기기 100 글자 넘기기 " +
                    "100 글자 넘기기 100 글자 넘기기 100 글자 넘기기 100 글자 넘기기 100 글자 넘기기");

            // then
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("title 최대입력 길이를 초과하였습니다.");
    }
}