package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
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
            DeleteHistory.ofQuestion(1L, writer)
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
                DeleteHistory.ofQuestion(1L, writer)
            )
        );

        // when
        deleteHistories.addAll(
            new DeleteHistories(
                Arrays.asList(
                    DeleteHistory.ofQuestion(2L, writer),
                    DeleteHistory.ofQuestion(3L, writer)
                )
            )
        );

        // then
        assertThat(deleteHistories.getValues()).hasSize(3);
    }
}
