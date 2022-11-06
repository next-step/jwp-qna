package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
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
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private User user;
    private Question question1;
    private Question question2;

    @BeforeEach
    public void init() {
        user = new User("taewon", "password", "name", "htw1800@naver.com");
        userRepository.save(user);
        question1 = new Question("title1", "contents1").writeBy(user);
        question2 = new Question("title2", "contents2").writeBy(user);
    }

    @Test
    @DisplayName("저장")
    public void save() {
        Question saved = saveAndRefetch(question1);
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getTitle()).isEqualTo(question1.getTitle()),
                () -> assertThat(saved.getWriterId()).isEqualTo(question1.getWriterId()),
                () -> assertThat(saved.getContents()).isEqualTo(question1.getContents())
        );
    }

    @Test
    @DisplayName("개별 조회 by id")
    public void findById() {
        Question saved = saveAndClear(question1);
        Optional<Question> optional = questionRepository.findById(saved.getId());
        assertThat(optional).isNotEmpty();
        Question fetched = optional.get();
        assertThat(fetched.getId()).isEqualTo(saved.getId());
    }

    @Test
    @DisplayName("개별 조회 by id, deleted(false)")
    public void findByIdAndDeletedFalse() {
        Question saved = saveAndClear(question1);
        Optional<Question> optional = questionRepository.findByIdAndDeletedFalse(saved.getId());
        assertThat(optional).isNotEmpty();
        Question fetched = optional.get();
        assertThat(fetched.getId()).isEqualTo(saved.getId());
        assertThat(fetched.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("목록 조회 by deleted(false)")
    public void findByDeletedFalse() {
        saveAndClear(question1);
        saveAndClear(question2);
        List<Question> actives = questionRepository.findByDeletedFalse();
        assertThat(actives).isNotEmpty();
        assertThat(actives.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("제거")
    public void delete() {
        Question saved = saveAndRefetch(question1);
        questionRepository.delete(saved);
        Optional<Question> optional = questionRepository.findById(saved.getId());
        assertThat(optional).isEmpty();
    }

    private Question saveAndRefetch(Question question) {
        Question saved = saveAndClear(question);
        return questionRepository.findById(saved.getId())
                .orElseThrow(() -> new NullPointerException("Question not saved!"));
    }

    private Question saveAndClear(Question question) {
        Question saved = questionRepository.save(question);
        testEntityManager.clear();
        return saved;
    }
}
