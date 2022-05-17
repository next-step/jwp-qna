package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import qna.config.QnaDataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.FixtureQuestion.Q1;
import static qna.domain.FixtureUser.HEOWC;

@QnaDataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository repository;

    @DisplayName("save하면 id 자동 생성")
    @Test
    void save() {
        final Question question = repository.save(Q1);
        assertThat(question.getId()).isNotNull();
        assertThat(question.getWriter()).isEqualTo(Q1.getWriter());
    }

    @DisplayName("저장한 엔티티와 동일한 id로 조회한 엔티티는 동일성 보장")
    @Test
    void sameEntity() {
        final Question saved = repository.save(Q1);
        final Question question = repository.findById(saved.getId()).get();
        assertThat(saved.getId()).isEqualTo(question.getId());
        assertThat(saved).isEqualTo(question);
    }

    @DisplayName("Question에 대한 User 변경")
    @Test
    void writeBy() {
        final Question question = new Question("dummy title", "dummy contents");
        question.writeBy(HEOWC);
        final Question saved = repository.save(question);
        assertThat(saved.getWriter()).isEqualTo(HEOWC);
    }
}
