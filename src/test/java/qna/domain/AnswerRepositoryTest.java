package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("AnswerRepository 클래스")
@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);
        questionRepository.save(QuestionTest.Q1);
        questionRepository.save(QuestionTest.Q2);
    }

    @DisplayName("저장")
    @Test
    void save() {
        final Answer saved = answerRepository.save(AnswerTest.A1);
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getCreatedAt()).isNotNull(),
                () -> assertThat(saved.getUpdatedAt()).isNotNull()
        );
    }

    @DisplayName("Answer Id 조회")
    @Test
    void findByIdAndDeletedFalse() {
        final Answer saved = answerRepository.save(AnswerTest.A1);
        final Answer finded = answerRepository.findByIdAndDeletedFalse(saved.getId()).get();
        assertThat(finded).isNotNull();
    }

    @DisplayName("Writer 조회")
    @Test
    void findByWriterIdAndDeletedFalse() {
        final Answer saved = answerRepository.save(AnswerTest.A1);
        final Answer finded = answerRepository.findByWriterAndDeletedFalse(saved.getWriter()).get(0);
        assertThat(finded).isNotNull();
    }

    @DisplayName("Question 조회")
    @Test
    void findByQuestionAndDeletedFalse() {
        final Answer saved = answerRepository.save(AnswerTest.A1);
        final Answer finded = answerRepository.findByQuestionAndDeletedFalse(QuestionTest.Q1).get(0);
        assertThat(finded).isNotNull();
    }

    @DisplayName("Contents 변경")
    @Test
    void updateContents() {
        final Answer saved = answerRepository.save(AnswerTest.A1);
        saved.setContents("updated");
        final Answer finded = answerRepository.findByIdAndDeletedFalse(saved.getId()).get();
        assertThat(finded.getContents()).isEqualTo("updated");
    }

    @DisplayName("Question 변경")
    @Test
    void updateQuestion() {
        final Answer saved = answerRepository.save(AnswerTest.A1);
        saved.toQuestion(QuestionTest.Q2);
        final Answer finded = answerRepository.findByIdAndDeletedFalse(saved.getId()).get();
        assertThat(finded.getQuestion()).isEqualTo(QuestionTest.Q2);
    }

    @DisplayName("Writer 변경")
    @Test
    void updateWriter() {
        final Answer saved = answerRepository.save(AnswerTest.A1);
        saved.setWriter(UserTest.SANJIGI);
        final Answer finded = answerRepository.findByIdAndDeletedFalse(saved.getId()).get();
        assertThat(finded.getWriter()).isEqualTo(UserTest.SANJIGI);
    }
}
