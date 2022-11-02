package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;
import qna.domain.QuestionTest;

@DataJpaTest
@DisplayName("Question")
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository repository;

    @Test
    @DisplayName("저장")
    public void save() {
        Question target = QuestionTest.Q1;
        Question saved = repository.save(target);
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getTitle()).isEqualTo(target.getTitle()),
                () -> assertThat(saved.getWriterId()).isEqualTo(target.getWriterId()),
                () -> assertThat(saved.getContents()).isEqualTo(target.getContents())
        );
    }

    @Test
    @DisplayName("개별 조회 by id")
    public void findById() {
        Question saved = repository.save(QuestionTest.Q1);
        Optional<Question> fetched = repository.findById(saved.getId());
        assertThat(fetched).isNotEmpty();
        assertThat(fetched.get()).isEqualTo(saved);
    }

    @Test
    @DisplayName("개별 조회 by id, deleted(false)")
    public void findByIdAndDeletedFalse() {
        Question target = QuestionTest.Q1;
        Question nonTarget = QuestionTest.Q2;
        repository.save(target);
        repository.save(nonTarget);
        Optional<Question> fetched = repository.findById(target.getId());
        assertThat(fetched).isNotEmpty();
        assertThat(fetched.get()).isEqualTo(target);
        assertThat(fetched).get().isNotEqualTo(nonTarget);
    }

    @Test
    @DisplayName("목록 조회 by deleted(false)")
    public void findByDeletedFalse() {
        repository.save(QuestionTest.Q1);
        repository.save(QuestionTest.Q2);
        List<Question> actives = repository.findByDeletedFalse();
        assertThat(actives.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("제거")
    public void delete() {
        Question saved = repository.save(QuestionTest.Q1);
        repository.delete(saved);
        Optional<Question> fetched = repository.findById(saved.getId());
        assertThat(fetched).isEmpty();
    }
}
