package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.AnswerTest.A1;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    private Answer savedAnswer;

    @BeforeEach
    private void beforeEach() {
        savedAnswer = answerRepository.save(A1);
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
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());
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
    public void updateAnswerDeletedTrue(){
        savedAnswer.setDeleted(true);
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
