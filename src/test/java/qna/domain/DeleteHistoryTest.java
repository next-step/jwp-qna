package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DisplayName("DeleteHistory 테스트")
class DeleteHistoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @DisplayName("Save 확인")
    @Test
    void save_확인() {
        // given
        DeleteHistory deleteHistory = DeleteHistoryTestFactory.create(ContentType.ANSWER, 1L, 1L);

        // when
        DeleteHistory actual = deleteHistoryRepository.save(deleteHistory);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(deleteHistory.getId()),
                () -> assertThat(actual.getContentType()).isEqualTo(deleteHistory.getContentType()),
                () -> assertThat(actual.getContentId()).isEqualTo(deleteHistory.getContentId()),
                () -> assertThat(actual.getDeletedById()).isEqualTo(deleteHistory.getDeletedById()),
                () -> assertThat(actual.getCreateDate()).isEqualTo(deleteHistory.getCreateDate())
        );
    }
}