package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    private Question question;

    @BeforeEach
    void setUp() {
        question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    }

    @Test
    @DisplayName("Question 생성 테스트")
    void create() {
        assertThat(question).isEqualTo(new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Question 의 contents 업데이트 시 빈 값 입력 하면 예외발생")
    void validateUpdateContents(String contents) {
        assertThatThrownBy(() -> question.updateContents(contents))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("null 또는 빈값으로 contents를 업데이트 할 수 없습니다.");
    }

    @Test
    @DisplayName("Question 의 contents 정상 업데이트 테스트")
    void updateContents() {
        String changedContents = "변경된 내용";

        question.updateContents(changedContents);

        assertThat(question.getContents()).isEqualTo(changedContents);
    }
}
