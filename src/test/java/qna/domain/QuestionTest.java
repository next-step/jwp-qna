package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.CannotDeleteException;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("Question 값 확인")
    @Test
    void init() {
        assertAll(
            () -> assertThat(Q1.getTitle()).isEqualTo("title1"),
            () -> assertThat(Q1.getContents()).isEqualTo("contents1"),
            () -> assertThat(Q1.getWriter()).isEqualTo(UserTest.JAVAJIGI)
        );
    }

    @DisplayName("질문 삭제")
    @Test
    void deleteQuestion() throws CannotDeleteException {
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

        question.delete(UserTest.JAVAJIGI);

        assertThat(question.isDeleted()).isTrue();
    }

    @DisplayName("삭제. 질문자와 로그인한 사용자가 다를 경우 오류")
    @Test
    void invalidDeleteQuestion() {
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() -> {
                Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

                question.delete(UserTest.SANJIGI);
            }).withMessageMatching("질문을 삭제할 권한이 없습니다.");
    }
}
