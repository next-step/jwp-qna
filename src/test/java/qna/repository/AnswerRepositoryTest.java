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
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.QuestionTest;
import qna.domain.User;

@DataJpaTest
@DisplayName("Answer")
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository repository;

    @Autowired
    private TestEntityManager testEntityManager;

    private static Answer A1;
    private static Answer A2;

    @BeforeAll
    public static void init() {
        User user = new User(1L, "taewon", "password", "name", "htw1800@naver.com");
        Question question = new Question("title1", "contents1").writeBy(user);
        A1 = new Answer(user, question, "Answers Contents1");
        A2 = new Answer(user, question, "Answers Contents2");
    }

    @Test
    @DisplayName("저장")
    public void save() {
        Answer saved = saveAndRefetch(A1);
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getContents()).isEqualTo(A1.getContents()),
                () -> assertThat(saved.getQuestionId()).isEqualTo(A1.getQuestionId()),
                () -> assertThat(saved.getWriterId()).isEqualTo(A1.getWriterId())
        );
    }

    @Test
    @DisplayName("개별 조회 by id")
    public void findById() {
        Answer saved = saveAndClear(A1);
        Optional<Answer> optional = repository.findById(saved.getId());
        assertThat(optional).isNotEmpty();
        Answer fetched = optional.get();
        assertThat(fetched.getId()).isEqualTo(saved.getId());
    }

    @Test
    @DisplayName("개별 조회 by id, deleted(false)")
    public void findByIdAndDeletedFalse() {
        Answer saved = saveAndClear(A1);
        Optional<Answer> optional = repository.findByIdAndDeletedFalse(saved.getId());
        assertThat(optional).isNotEmpty();
        Answer fetched = optional.get();
        assertThat(fetched.getId()).isEqualTo(saved.getId());
    }

    @Test
    @DisplayName("목록 조회 by questionId, deleted(false)")
    public void findByQuestionIdAndDeletedFalse() {
        saveAndClear(A1);
        saveAndClear(A2);
        List<Answer> actives = repository.findByQuestionIdAndDeletedFalse(A1.getQuestionId());
        assertThat(actives).isNotEmpty();
        assertThat(actives.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("제거")
    public void delete() {
        Answer saved = saveAndRefetch(A1);
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
