package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import qna.config.QnaDataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.FixtureAnswer.A1;
import static qna.domain.FixtureQuestion.Q1;
import static qna.domain.FixtureUser.HEOWC;

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

    @DisplayName("Answer에 대한 Question을 변경")
    @Test
    void toQuestion() {
        final Answer answer = new Answer(HEOWC, Q1, "dummy");
        answer.toQuestion(new Question(3L, "3", "3"));
        final Answer saved = repository.save(answer);
        assertThat(saved.getQuestionId()).isEqualTo(3L);
    }
}
