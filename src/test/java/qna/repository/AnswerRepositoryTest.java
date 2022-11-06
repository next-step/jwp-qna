package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

@DataJpaTest
@DisplayName("Answer")
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private User user;
    private Answer answer1;
    private Answer answer2;

    @BeforeEach
    public void init() {
        user = new User("taewon", "password", "name", "htw1800@naver.com");
        userRepository.save(user);
        Question question = new Question("title1", "contents1").writeBy(user);
        questionRepository.save(question);
        answer1 = new Answer(user, question, "Answers Contents1");
        answer2 = new Answer(user, question, "Answers Contents2");
    }

    @Test
    @DisplayName("저장")
    public void save() {
        Answer saved = saveAndRefetch(answer1);
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getContents()).isEqualTo(answer1.getContents()),
                () -> assertThat(saved.getQuestion().getId()).isEqualTo(answer1.getQuestion().getId()),
                () -> assertThat(saved.getWriter()).isEqualTo(answer1.getWriter())
        );
    }

    @Test
    @DisplayName("개별 조회 by id")
    public void findById() {
        Answer saved = saveAndClear(answer1);
        Optional<Answer> optional = answerRepository.findById(saved.getId());
        assertThat(optional).isNotEmpty();
        Answer fetched = optional.get();
        assertThat(fetched.getId()).isEqualTo(saved.getId());
    }

    @Test
    @DisplayName("개별 조회 by id, deleted(false)")
    public void findByIdAndDeletedFalse() {
        Answer saved = saveAndClear(answer1);
        Optional<Answer> optional = answerRepository.findByIdAndDeletedFalse(saved.getId());
        assertThat(optional).isNotEmpty();
        Answer fetched = optional.get();
        assertThat(fetched.getId()).isEqualTo(saved.getId());
    }

    @Test
    @DisplayName("제거")
    public void delete() {
        Answer saved = saveAndRefetch(answer1);
        answerRepository.delete(saved);
        Optional<Answer> optional = answerRepository.findById(saved.getId());
        assertThat(optional).isEmpty();
    }

    private Answer saveAndRefetch(Answer answer) {
        Answer saved = saveAndClear(answer);
        return answerRepository.findById(saved.getId())
                .orElseThrow(() -> new NullPointerException("Answer not saved!"));
    }

    private Answer saveAndClear(Answer answer) {
        Answer saved = answerRepository.save(answer);
        testEntityManager.clear();
        return saved;
    }
}
