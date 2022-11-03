package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.AnswerTest.A1;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answerRepository;

    @BeforeEach
    void setUp() {
        answerRepository.deleteAll();
    }

    @Test
    @DisplayName("questionId와 일치하고 삭제상태가 false인 Answer목록을 반환")
    void findByQuestionIdAndDeletedFalse() {
        Answer savedAnswer = answerRepository.save(A1);

        List<Answer> findAnswer = answerRepository.findByQuestionIdAndDeletedFalse(savedAnswer.getQuestionId());

        assertThat(findAnswer).containsExactly(savedAnswer);
    }

    @Test
    @DisplayName("Answer의 id와 일치하고 삭제상태가 false인 Answer를 반환")
    void findByIdAndDeletedFalse() {
        answerRepository.save(A1);

        Optional<Answer> answer = answerRepository.findByIdAndDeletedFalse(A1.getId());

        assertThat(answer).contains(A1);
    }

}
