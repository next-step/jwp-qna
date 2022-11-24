package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("질문 삭제 권한 없는 경우 예외처리")
    @Test
    void 질문삭제_권한_예외처리() {
        assertAll(
                () -> Q1.checkQuestionDeleteAuth(UserTest.JAVAJIGI),
                () -> assertThatThrownBy(() -> Q1.checkQuestionDeleteAuth(UserTest.SANJIGI))
                        .isInstanceOf(CannotDeleteException.class)
        );
    }

}
