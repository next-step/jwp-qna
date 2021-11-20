package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class DeleteHistorysTest {
    @Test
    void init() {
        DeleteHistorys deleteHistorys = new DeleteHistorys(Arrays.asList(new DeleteHistory()));

        assertThat(deleteHistorys).isNotNull();
    }
}
