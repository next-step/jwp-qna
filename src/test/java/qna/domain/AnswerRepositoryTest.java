package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import qna.config.QnaDataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.AnswerTest.A1;

@QnaDataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository repository;

    @DisplayName("save하면 id 자동 생성")
    @Test
    void save() {
        final Answer answer = repository.save(A1);
        assertThat(answer.getId()).isNotNull();
    }

    @DisplayName("저장한 엔티티와 동일한 id로 조회한 엔티티는 동일성 보장")
    @Test
    void sameEntity() {
        final Answer saved = repository.save(A1);
        final Answer answer = repository.findById(saved.getId()).get();
        assertThat(answer.getId()).isEqualTo(saved.getId());
        assertThat(answer).isEqualTo(saved);
    }
}
