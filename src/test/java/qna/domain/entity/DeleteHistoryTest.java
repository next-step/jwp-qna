package qna.domain.entity;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class DeleteHistoryTest {

    @Test
    public void init() {
        DeleteHistory actual = DeleteHistory.ContentType.QUESTION.getDeleteHistory(1L, UserTest.USER_JAVAJIGI);

        assertAll(
            () -> assertThat(actual.getUser()).isEqualTo(UserTest.USER_JAVAJIGI)
        );
    }

}