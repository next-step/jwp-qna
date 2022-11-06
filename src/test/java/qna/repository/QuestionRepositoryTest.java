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

    private static Question question;

    @BeforeAll
    public static void init() {
        User user = new User(1L, "taewon", "password", "name", "htw1800@naver.com");
        question = new Question("title1", "contents1").writeBy(user);
    }

    @Test
    @DisplayName("저장")
    public void save() {
        Question saved = saveAndRefetch(question);
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getTitle()).isEqualTo(question.getTitle()),
                () -> assertThat(saved.getWriterId()).isEqualTo(question.getWriterId()),
                () -> assertThat(saved.getContents()).isEqualTo(question.getContents())
        );
    }

    @Test
    @DisplayName("개별 조회 by id")
    public void findById() {
        Question saved = saveAndClear(question);
        Optional<Question> optional = repository.findById(saved.getId());
        assertThat(optional).isNotEmpty();
        Question fetched = optional.get();
        assertThat(fetched.getId()).isEqualTo(saved.getId());
    }

    @Test
    @DisplayName("개별 조회 by id, deleted(false)")
    public void findByIdAndDeletedFalse() {
        Question saved = saveAndClear(question);
        Optional<Question> optional = repository.findByIdAndDeletedFalse(saved.getId());
        assertThat(optional).isNotEmpty();
        Question fetched = optional.get();
        assertThat(fetched.getId()).isEqualTo(saved.getId());
        assertThat(fetched.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("목록 조회 by deleted(false)")
    public void findByDeletedFalse() {
        saveAndClear(question);
        saveAndClear(QuestionTest.Q2);
        List<Question> actives = repository.findByDeletedFalse();
        assertThat(actives).isNotEmpty();
        assertThat(actives.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("제거")
    public void delete() {
        Question saved = saveAndRefetch(question);
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
