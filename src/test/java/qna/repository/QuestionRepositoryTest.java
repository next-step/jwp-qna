package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.domain.Question;
import qna.domain.QuestionTest;
import qna.domain.User;

@DataJpaTest
@DisplayName("Question")
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository repository;

    @Autowired
    private TestEntityManager testEntityManager;

    private static Question Q1;

    @BeforeAll
    public static void init() {
        User user = new User(1L, "taewon", "password", "name", "htw1800@naver.com");
        Q1 = new Question("title1", "contents1").writeBy(user);
    }

    @Test
    @DisplayName("저장")
    public void save() {
        Question saved = saveAndRefetch(Q1);
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getTitle()).isEqualTo(Q1.getTitle()),
                () -> assertThat(saved.getWriterId()).isEqualTo(Q1.getWriterId()),
                () -> assertThat(saved.getContents()).isEqualTo(Q1.getContents())
        );
    }

    @Test
    @DisplayName("개별 조회 by id")
    public void findById() {
        Question saved = saveAndClear(Q1);
        Optional<Question> optional = repository.findById(saved.getId());
        assertThat(optional).isNotEmpty();
        Question fetched = optional.get();
        assertThat(fetched.getId()).isEqualTo(saved.getId());
    }

    @Test
    @DisplayName("개별 조회 by id, deleted(false)")
    public void findByIdAndDeletedFalse() {
        Question saved = saveAndClear(Q1);
        Optional<Question> optional = repository.findByIdAndDeletedFalse(saved.getId());
        assertThat(optional).isNotEmpty();
        Question fetched = optional.get();
        assertThat(fetched.getId()).isEqualTo(saved.getId());
        assertThat(fetched.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("목록 조회 by deleted(false)")
    public void findByDeletedFalse() {
        saveAndClear(Q1);
        saveAndClear(QuestionTest.Q2);
        List<Question> actives = repository.findByDeletedFalse();
        assertThat(actives).isNotEmpty();
        assertThat(actives.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("제거")
    public void delete() {
        Question saved = saveAndRefetch(Q1);
        repository.delete(saved);
        Optional<Question> optional = repository.findById(saved.getId());
        assertThat(optional).isEmpty();
    }

    private Question saveAndRefetch(Question question) {
        Question saved = saveAndClear(question);
        return repository.findById(saved.getId())
                .orElseThrow(() -> new NullPointerException("Question not saved!"));
    }

    private Question saveAndClear(Question question) {
        Question saved = repository.save(question);
        testEntityManager.clear();
        return saved;
    }
}
