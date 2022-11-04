package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.domain.Answer;
import qna.domain.AnswerTest;

@DataJpaTest
@DisplayName("Answer")
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository repository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("저장")
    public void save() {
        Answer target = AnswerTest.A1;
        Answer saved = saveAndRefetch(target);
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getContents()).isEqualTo(target.getContents()),
                () -> assertThat(saved.getQuestionId()).isEqualTo(target.getQuestionId()),
                () -> assertThat(saved.getWriterId()).isEqualTo(target.getWriterId())
        );
    }

    @Test
    @DisplayName("개별 조회 by id")
    public void findById() {
        Answer saved = saveAndClear(AnswerTest.A1);
        Optional<Answer> optional = repository.findById(saved.getId());
        assertThat(optional).isNotEmpty();
        Answer fetched = optional.get();
        assertThat(fetched.getId()).isEqualTo(saved.getId());
    }

    @Test
    @DisplayName("개별 조회 by id, deleted(false)")
    public void findByIdAndDeletedFalse() {
        Answer saved = saveAndClear(AnswerTest.A1);
        Optional<Answer> optional = repository.findByIdAndDeletedFalse(saved.getId());
        assertThat(optional).isNotEmpty();
        Answer fetched = optional.get();
        assertThat(fetched.getId()).isEqualTo(saved.getId());
    }

    @Test
    @DisplayName("목록 조회 by questionId, deleted(false)")
    public void findByQuestionIdAndDeletedFalse() {
        saveAndClear(AnswerTest.A1);
        saveAndClear(AnswerTest.A2);
        List<Answer> actives = repository.findByQuestionIdAndDeletedFalse(AnswerTest.A1.getQuestionId());
        assertThat(actives).isNotEmpty();
        assertThat(actives.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("제거")
    public void delete() {
        Answer saved = saveAndRefetch(AnswerTest.A1);
        repository.delete(saved);
        Optional<Answer> optional = repository.findById(saved.getId());
        assertThat(optional).isEmpty();
    }

    private Answer saveAndRefetch(Answer answer) {
        Answer saved = saveAndClear(answer);
        return repository.findById(saved.getId())
                .orElseThrow(() -> new NullPointerException("Answer not saved!"));
    }

    private Answer saveAndClear(Answer answer) {
        Answer saved = repository.save(answer);
        testEntityManager.clear();
        return saved;
    }
}
