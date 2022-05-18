package qna.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1");
    public static final Question Q2 = new Question("title2", "contents2");

    @Test
    @DisplayName("toDeleted() 메소드를 이용해 Question을 삭제상태로 변경할 수 있다.")
    void domain_test_01(){
        // given & when
        Q1.toDeleted();

        // then
        assertAll(
            () -> assertTrue(Q1.isDeleted()),
            () -> assertFalse(Q2.isDeleted())
        );
    }
}
