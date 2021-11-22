package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
        final DeleteHistories newDeleteHistories = deleteHistories.add(
            DeleteHistory.ofQuestion(1L, writer)
        );

        // then
        assertAll(
            () -> assertThat(deleteHistories.getValues()).hasSize(0),
            () -> assertThat(newDeleteHistories.getValues()).hasSize(1)
        );
    }
}
