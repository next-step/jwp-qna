package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.domain.AnswerTest;
import qna.domain.QuestionTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("save 검증 테스트")
    void saveTest() {
        Answer answer = AnswerTest.A1;
        Answer savedanswer = answerRepository.save(answer);

        assertAll(
            () -> assertNotNull(savedanswer.getId()),
            () -> assertEquals(savedanswer.getWriterId(), answer.getWriterId()),
            () -> assertEquals(savedanswer.getQuestionId(), answer.getQuestionId())
        );
    }

    @Test
    @DisplayName("findByQuestionIdAndDeletedFalse 검증 테스트")
    void findByQuestionIdAndDeletedFalseTest() {
        answerRepository.save(AnswerTest.A1);

        List<Answer> answersFound =
            answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());

        assertAll(
            () -> assertThat(answersFound).hasSize(1),
            () -> assertTrue(answersFound.stream().noneMatch(Answer::isDeleted))
        );
    }

    @Test
    @DisplayName("findByIdAndDeletedFalse 검증 테스트")
    void findByIdAndDeletedFalseTest() {
        Answer answerSaved = answerRepository.save(AnswerTest.A1);
        Answer answerFound = answerRepository.findByIdAndDeletedFalse(answerSaved.getId()).get();

        assertFalse(answerFound.isDeleted());
    }

}
