package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Question 테스트")
public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("질문을 삭제한다.")
    void delete() {
        // when
        Q2.delete(UserTest.SANJIGI);

        // then
        assertThat(Q2.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문자가 아닌 사용자가 질문을 삭제하면 예외가 발생한다.")
    void deleteThrowException() {
        // when & then
        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() ->  Q1.delete(UserTest.SANJIGI))
                .withMessageMatching("질문을 삭제할 권한이 없습니다.");
    }
}
