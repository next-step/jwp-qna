package qna.domain;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@TestInstance(Lifecycle.PER_CLASS)
@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository UserRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeAll
    private void beforeAll() {
        UserRepository.save(UserTest.JAVAJIGI);
        UserRepository.save(UserTest.SANJIGI);

        questionRepository.save(QuestionTest.Q1);

        answerRepository.save(A1);
        answerRepository.save(A2);
    }

    @DisplayName("특정 질문에대한 삭제되지않은 답을 조회한다.")
    @Test
    public void find_forQuestion_NotDelete() {
        // given

        // when
        List<Answer> answers = answerRepository.findByQuestionAndDeletedFalse(QuestionTest.Q1);

        //then
        Assertions.assertThat(answers.size()).isEqualTo(2);

        assertAll(
            () -> Assertions.assertThat(answers.get(0).getWriter()).isEqualTo(A1.getWriter()),
            () -> Assertions.assertThat(answers.get(0).getQuestionId()).isEqualTo(A1.getQuestionId()),
            () -> Assertions.assertThat(answers.get(0).getContents()).isEqualTo(A1.getContents())
        );

        assertAll(
            () -> Assertions.assertThat(answers.get(1).getWriter().getName()).isEqualTo(A2.getWriter().getName()),
            () -> Assertions.assertThat(answers.get(1).getQuestionId()).isEqualTo(A2.getQuestionId()),
            () -> Assertions.assertThat(answers.get(1).getContents()).isEqualTo(A2.getContents())
        );
    }

    @DisplayName("삭제되지않은 답을 조회한다")
    @Test
    public void find_notDelete() {
        // given

        // when
        Optional<Answer> answers = answerRepository.findByIdAndDeletedFalse(A1.getId());

        //then
        Assertions.assertThat(answers).isPresent();

        assertAll(
            () -> Assertions.assertThat(answers.get().getWriter().getName()).isEqualTo(A1.getWriter().getName()),
            () -> Assertions.assertThat(answers.get().getQuestionId()).isEqualTo(A1.getQuestionId()),
            () -> Assertions.assertThat(answers.get().getContents()).isEqualTo(A1.getContents())
        );
    }
}
