package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswersTest {

    @Test
    @DisplayName("생성자에서 null값 입력시 예외발생")
    public void test_throw_exception() {
        assertThatThrownBy(() -> new Answers(null))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("질문목록이 없습니다");
    }
}
