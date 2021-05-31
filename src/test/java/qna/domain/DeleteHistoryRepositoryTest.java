package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository delHistorys;

    @DisplayName("DeleteHistroy 저장 테스트")
    @Test
    void save() {
        DeleteHistory actual = delHistorys.save(DeleteHistoryTest.DH1);
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getContentType()).isEqualTo(ContentType.ANSWER)
        );
    }
}
