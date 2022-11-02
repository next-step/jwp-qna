package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.AnswerTest;

@DataJpaTest
@DisplayName("Answer")
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository repository;

    @Test
    @DisplayName("저장")
    public void save() {
        Answer target = AnswerTest.A1;
        Answer saved = repository.save(target);
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
        Answer saved = repository.save(AnswerTest.A1);
        Optional<Answer> fetched = repository.findById(saved.getId());
        assertThat(fetched).isNotEmpty();
        assertThat(fetched.get()).isEqualTo(saved);
    }

    @Test
    @DisplayName("개별 조회 by id, deleted(false)")
    public void findByIdAndDeletedFalse() {
        Answer target = AnswerTest.A1;
        Answer nonTarget = AnswerTest.A2;
        repository.save(target);
        repository.save(nonTarget);
        Optional<Answer> fetched = repository.findById(target.getId());
        assertThat(fetched).isNotEmpty();
        assertThat(fetched.get()).isEqualTo(target);
        assertThat(fetched).get().isNotEqualTo(nonTarget);
    }

    @Test
    @DisplayName("목록 조회 by questionId, deleted(false)")
    public void findByQuestionIdAndDeletedFalse() {
        repository.save(AnswerTest.A1);
        repository.save(AnswerTest.A2);
        List<Answer> actives = repository.findByQuestionIdAndDeletedFalse(AnswerTest.A1.getQuestionId());
        assertThat(AnswerTest.A1.getQuestionId()).isEqualTo(AnswerTest.A2.getQuestionId());
        assertThat(actives).isNotEmpty();
        assertThat(actives.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("제거")
    public void delete() {
        Answer saved = repository.save(AnswerTest.A1);
        repository.delete(saved);
        Optional<Answer> fetched = repository.findById(saved.getId());
        assertThat(fetched).isEmpty();
    }
}
