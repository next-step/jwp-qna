package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;
    private User user;
    private Question question;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        question = questionRepository.save(new Question("title1", "contents1").writeBy(user));
    }

    @Test
    void 답변_저장() {
        Answer answer = new Answer(user, question, "Answers Contents1");
        Answer actual = answerRepository.save(answer);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriter()).isEqualTo(user),
                () -> assertThat(actual.getQuestion()).isEqualTo(question),
                () -> assertThat(actual.getContents()).isEqualTo(answer.getContents()),
                () -> assertThat(actual.isDeleted()).isFalse(),
                () -> assertThat(actual.getCreatedAt()).isNotNull(),
                () -> assertThat(actual.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    void 답변_저장_후_조회() {
        Answer answer = answerRepository.save(new Answer(user, question, "Answers Contents1"));
        Answer actual = answerRepository.findById(answer.getId()).orElseThrow(EntityNotFoundException::new);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriter()).isEqualTo(user),
                () -> assertThat(actual.getQuestion()).isEqualTo(question),
                () -> assertThat(actual.getContents()).isEqualTo(answer.getContents()),
                () -> assertThat(actual.isDeleted()).isFalse(),
                () -> assertThat(actual.getCreatedAt()).isNotNull(),
                () -> assertThat(actual.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    void 답변_삭제() {
        Answer answer = answerRepository.save(new Answer(user, question, "Answers Contents1"));
        answerRepository.deleteById(answer.getId());
        assertThat(answerRepository.findByIdAndDeletedFalse(answer.getId())).isNotPresent();
    }

    @Test
    void ID로_답변_조회() {
        answerRepository.save(new Answer(user, question, "Answers Contents1"));
        List<Answer> actual = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());
        assertThat(actual).hasSize(1);
    }

    @Test
    void 삭제되지_않은_답변_조회() {
        Answer answer = answerRepository.save(new Answer(user, question, "Answers Contents1"));
        Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(answer.getId());
        assertThat(actual).isPresent();
    }

    @Test
    void 영속성_초기화후_같은객채_조회() {
        Answer answer = answerRepository.save(new Answer(user, question, "Answers Contents1"));
        flushAndClear();
        Answer actual = answerRepository.findById(answer.getId()).get();
        assertThat(actual).isEqualTo(answer);
    }

    @Test
    void 답변삭제시_상태변경() {
        Answer answer = answerRepository.save(new Answer(user, question, "Answers Contents1"));
        answer.delete(user);
        flushAndClear();
        Answer actual = answerRepository.findById(answer.getId()).get();
        assertThat(actual.isDeleted()).isTrue();
    }

    private void flushAndClear() {
        testEntityManager.flush();
        testEntityManager.clear();
    }
}
