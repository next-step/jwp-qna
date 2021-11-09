package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Question 테스트")
public class QuestionTest {

    public static final Question TWO_ANSWERED_QUESTION = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question ONE_ANSWERED_QUESTION = new Question("title2", "contents2").writeBy(UserTest.JAVAJIGI);
    public static final Question NOT_ANSWERED_QUESTION = new Question("title3", "contents3").writeBy(UserTest.JAVAJIGI);

    @Test
    @DisplayName("답변이 있는 질문을 삭제한다.")
    void delete1() {
        // when
        ONE_ANSWERED_QUESTION.delete(UserTest.JAVAJIGI);

        // then
        assertThat(ONE_ANSWERED_QUESTION.isDeleted()).isTrue();
    }
    
    @Test
    @DisplayName("답변이 없는 질문을 삭제한다.")
    void delete2() {
        // when
        NOT_ANSWERED_QUESTION.delete(UserTest.JAVAJIGI);

        // then
        assertThat(NOT_ANSWERED_QUESTION.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문자가 아닌 사용자가 질문을 삭제하면 예외가 발생한다.")
    void deleteThrowException() {
        // when & then
        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() ->  TWO_ANSWERED_QUESTION.delete(UserTest.SANJIGI))
                .withMessageMatching("질문을 삭제할 권한이 없습니다.");
    }
}
