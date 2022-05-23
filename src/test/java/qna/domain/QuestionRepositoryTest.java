package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DisplayName("QuestionRepository 클래스")
@DataJpaTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);
    }

    @DisplayName("저장")
    @Test
    void save() {
        final Question saved = questionRepository.save(QuestionTest.Q1);
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getCreatedAt()).isNotNull(),
                () -> assertThat(saved.getUpdatedAt()).isNotNull()
        );
    }

    @DisplayName("Question Id 조회")
    @Test
    void findByIdAndDeletedFalse() {
        final Question saved = questionRepository.save(QuestionTest.Q1);
        final Question finded = questionRepository.findByIdAndDeletedFalse(saved.getId()).get();
        assertThat(finded).isNotNull();
    }

    @DisplayName("Writer 조회")
    @Test
    void findByWriterAndDeletedFalse() {
        questionRepository.save(QuestionTest.Q1);
        final Question finded = questionRepository.findByWriterAndDeletedFalse(UserTest.JAVAJIGI).get(0);
        assertThat(finded).isNotNull();
    }

    @DisplayName("Writer 변경")
    @Test
    void updateWriter() {
        final Question saved = questionRepository.save(QuestionTest.Q1);
        saved.writeBy(UserTest.SANJIGI);
        final Question finded = questionRepository.findByIdAndDeletedFalse(saved.getId()).get();
        assertThat(finded.getWriter()).isEqualTo(UserTest.SANJIGI);
    }

    @DisplayName("Answer 조회")
    @Test
    void findByIdAndGetAnswers() {
        final Question saved = questionRepository.save(QuestionTest.Q1);
        saved.addAnswer(answerRepository.save(AnswerTest.A1));
        saved.addAnswer(answerRepository.save(AnswerTest.A2));

        final Question finded = questionRepository.findByIdAndDeletedFalse(saved.getId()).get();
        final List<Answer> answers = finded.getAnswers();
        assertAll(
                () -> assertThat(answers.get(0).getQuestion()).isEqualTo(finded),
                () -> assertThat(answers.get(1).getQuestion()).isEqualTo(finded),
                () -> assertThat(answers.size()).isEqualTo(2)
        );
    }
}
