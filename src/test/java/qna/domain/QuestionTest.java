package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1");
    public static final Question Q2 = new Question("title2", "contents2");

    @BeforeEach
    void setUp() {
        Q1.writeBy(UserTest.JAVAJIGI);
        Q2.writeBy(UserTest.SANJIGI);
    }

    @Test
    @DisplayName("toDeleted() 메소드를 이용해 Question을 삭제상태로 변경할 수 있다.")
    void domain_test_01() throws CannotDeleteException {
        // given & when
        Q1.toDeleted(UserTest.JAVAJIGI, Lists.newArrayList(AnswerTest.A1));

        // then
        assertAll(
            () -> assertTrue(Q1.isDeleted()),
            () -> assertFalse(Q2.isDeleted())
        );
    }

    @Test
    @DisplayName("본인이 작성한 질문이 아니면 CannotDeleteException 이 발생한다.")
    void domain_test_02() {
        // given & when & then
        assertThatThrownBy(() -> Q1.toDeleted(UserTest.SANJIGI, Lists.newArrayList(AnswerTest.A2)))
            .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("본인이 작성한 질문에 다른 사용자가 답변을 남긴 경우 질문 삭제 시 CannotDeleteException 이 발생한다.")
    void domain_test_03() {
        // given & when & then
        assertThatThrownBy(() -> Q1.toDeleted(UserTest.JAVAJIGI, Lists.newArrayList(AnswerTest.A2)))
            .isInstanceOf(CannotDeleteException.class);
    }
}
