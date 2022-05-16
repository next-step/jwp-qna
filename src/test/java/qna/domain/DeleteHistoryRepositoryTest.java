package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import qna.config.QnaDataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.FixtureDeleteHistory.DH_A1;
import static qna.domain.FixtureDeleteHistory.DH_Q1;

@QnaDataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository repository;

    @DisplayName("save하면 id 자동 생성")
    @Test
    void save() {
        final DeleteHistory deleteHistory = repository.save(DH_A1);
        assertThat(deleteHistory.getId()).isNotNull();
    }

    @DisplayName("저장한 엔티티와 동일한 id로 조회한 엔티티는 동일성 보장")
    @Test
    void sameEntity() {
        final DeleteHistory saved = repository.save(DH_Q1);
        final DeleteHistory deleteHistory = repository.findById(saved.getId()).get();
        assertThat(deleteHistory.getId()).isEqualTo(saved.getId());
        assertThat(deleteHistory).isEqualTo(saved);
    }
}
