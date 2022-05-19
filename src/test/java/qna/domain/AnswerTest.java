package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.repository.AnswerRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    private Answer saveA1;

    @BeforeAll
    void setUp() {
        saveA1 = answerRepository.save(A1);
    }

    @Test
    @DisplayName("Answer save 테스트")
    @Order(0)
    void save() {
        assertThat(saveA1).isNotNull();
    }

    @Test
    @DisplayName("questionId update 테스트")
    @Order(1)
    void update_toQuestion() {
        saveA1.toQuestion(QuestionTest.Q1);
        answerRepository.flush();
        assertThat(QuestionTest.Q1.getId()).isEqualTo(saveA1.getQuestionId());
    }

    @Test
    @DisplayName("Answer select 테스트")
    @Order(2)
    void select_findByQuestionIdAndDeletedFalse() {
        List<Answer> actual =
            answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());

        assertThat(actual.get(0).getId()).isEqualTo(saveA1.getId());
    }

    @Test
    @DisplayName("Answer select 테스트")
    @Order(3)
    void select_findByIdAndDeletedFalse() {
        Optional<Answer> actual =
            answerRepository.findByIdAndDeletedFalse(saveA1.getId());

        assertThat(actual.get().getId()).isEqualTo(saveA1.getId());
    }

    @Test
    @DisplayName("Answer save 예외 테스트")
    void save_UnAuthorizedException() {
        assertThatExceptionOfType(UnAuthorizedException.class)
            .isThrownBy(() -> new Answer(1L, null, QuestionTest.Q1, "Answers Contents1"));
    }

    @Test
    @DisplayName("Answer save 예외 테스트")
    void save_NotFoundException() {
        assertThatExceptionOfType(NotFoundException.class)
            .isThrownBy(() -> new Answer(1L, UserTest.JAVAJIGI, null, "Answers Contents1"));
    }

    @Test
    @DisplayName("Answer delete 테스트")
    @Order(Integer.MAX_VALUE)
    void delete() {
        answerRepository.delete(saveA1);
        Optional<Answer> actual = answerRepository.findById(saveA1.getId());
        assertThat(actual.isPresent()).isFalse();
    }

}
