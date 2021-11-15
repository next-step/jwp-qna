package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistories;

    @Test
    void save() {
        final DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
        final DeleteHistory actual = deleteHistories.save(expected);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContentId()).isEqualTo(expected.getContentId())
        );
    }

    @Test
    void findByContentsContainingTest() {
        Long expected = 1L;
        deleteHistories.save(new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now()));
        Long actual = deleteHistories.findByContentId(expected).getContentId();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void test() {
    }

}