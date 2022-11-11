package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.enumType.ContentType.QUESTION;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryTest {
    private static final DeleteHistory D1 = new DeleteHistory(QUESTION, 1L, JAVAJIGI);

    @Test
    void 값_검증() {
        assertAll(
                () -> assertThat(D1.getContentType()).isEqualTo(QUESTION),
                () -> assertThat(D1.getContentId()).isEqualTo(1L),
                () -> assertThat(D1.getDeletedBy()).isEqualTo(1L)
        );
    }
}
