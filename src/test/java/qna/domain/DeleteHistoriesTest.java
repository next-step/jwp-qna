package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class DeleteHistoriesTest {

    @Test
    void add() {
        // given
        final User writer = TestUserFactory.create(
            1L, "javajigi", "password", "name", "javajigi@slipp.net"
        );
        final DeleteHistories deleteHistories = new DeleteHistories();

        // when
        deleteHistories.add(
            new DeleteHistory(ContentType.QUESTION, 1L, writer, LocalDateTime.now())
        );

        // then
        assertThat(deleteHistories.getValues()).hasSize(1);
    }

    @Test
    void addAll() {
        // given
        final User writer = TestUserFactory.create(
            1L, "javajigi", "password", "name", "javajigi@slipp.net"
        );
        final DeleteHistories deleteHistories = new DeleteHistories(
            Arrays.asList(
                new DeleteHistory(ContentType.QUESTION, 1L, writer, LocalDateTime.now())
            )
        );

        // when
        deleteHistories.addAll(
            new DeleteHistories(
                Arrays.asList(
                    new DeleteHistory(ContentType.QUESTION, 2L, writer, LocalDateTime.now()),
                    new DeleteHistory(ContentType.QUESTION, 3L, writer, LocalDateTime.now())
                )
            )
        );

        // then
        assertThat(deleteHistories.getValues()).hasSize(3);
    }
}
