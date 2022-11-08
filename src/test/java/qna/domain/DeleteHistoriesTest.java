package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

class DeleteHistoriesTest {

    @Test
    void concat() {
        DeleteHistories deleteHistories1 = new DeleteHistories(Collections.singletonList(new DeleteHistory()));
        DeleteHistories deleteHistories2 = new DeleteHistories(Arrays.asList(new DeleteHistory(), new DeleteHistory()));

        DeleteHistories actual = deleteHistories1.concat(deleteHistories2);
        assertThat(actual.getList()).hasSize(3);
    }
}
