package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import qna.config.QnaDataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.FixtureQuestion.Q1;

@QnaDataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository repository;

    @DisplayName("save하면 id 자동 생성")
    @Test
    void save() {
        final Question question = repository.save(Q1);
        assertThat(question.getId()).isNotNull();
        assertThat(question.getWriterId()).isEqualTo(Q1.getWriterId());
    }

    @DisplayName("저장한 엔티티와 동일한 id로 조회한 엔티티는 동일성 보장")
    @Test
    void sameEntity() {
        final Question saved = repository.save(Q1);
        final Question question = repository.findById(saved.getId()).get();
        assertThat(saved.getId()).isEqualTo(question.getId());
        assertThat(saved).isEqualTo(question);
    }
}
