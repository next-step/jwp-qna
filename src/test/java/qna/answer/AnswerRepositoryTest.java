package qna.answer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.PreExecutionTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AnswerRepositoryTest extends PreExecutionTest {
    @Autowired
    private AnswerRepository answerRepository;

    @BeforeEach
    private void beforeEach() {
        Answer actual = new Answer(savedUser, savedQuestion, "Answers Contents1");
        savedAnswer = answerRepository.save(actual);
    }

    @Test
    @DisplayName("answer 등록")
    public void saveAnswerTest() {
        Answer answer = answerRepository.findById(savedAnswer.getId()).get();
        assertAll(
                () -> assertNotNull(savedAnswer.getId()),
                () -> assertEquals(answer, savedAnswer)
        );
    }

    @Test
    @DisplayName("question id로 deleted가 false인 answer 검색")
    public void findByQuestionIdAndDeletedFalseTest() {
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(
                savedAnswer.getQuestion()
                        .getId());
        assertThat(answers).containsExactly(savedAnswer);
    }

    @Test
    @DisplayName("answer id로 deleted가 false인 answer 단일검색")
    public void findByIdAndDeletedFalseTest() {
        Optional<Answer> oAnswer = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId());
        assertAnswerDeleted(oAnswer, false);
    }

    @Test
    @DisplayName("answer에 delete를 true로 수정")
    public void updateAnswerDeletedTrue() {
        savedAnswer.delete();
        Optional<Answer> oAnswer = answerRepository.findById(savedAnswer.getId());
        assertAnswerDeleted(oAnswer, true);
    }

    private void assertAnswerDeleted(Optional<Answer> oAnswer, boolean isDeleted) {
        assertAll(
                () -> assertNotNull(oAnswer),
                () -> assertEquals(oAnswer.get(), savedAnswer),
                () -> assertEquals(oAnswer.get().isDeleted(), isDeleted)
        );
    }
}
