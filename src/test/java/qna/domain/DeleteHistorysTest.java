package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DeleteHistorysTest {

    private DeleteHistorys deleteHistorys;

    @BeforeEach
    void setUp() {
        List<DeleteHistory> deleteList = new ArrayList<>();
        deleteList.add(DeleteHistory.of(ContentType.ANSWER, 1L, User.of("tjrwls1", "12", "김석진", "7271kim@naver.com")));
        deleteList.add(DeleteHistory.of(ContentType.ANSWER, 1L, User.of("tjrwls2", "12", "김석진", "7271kim2@naver.com")));

        deleteHistorys = DeleteHistorys.of(deleteList);
    }

    @Test
    @DisplayName("생성 테스트")
    void create() {
        assertThat(deleteHistorys.size()).isEqualTo(2);
    }
}
