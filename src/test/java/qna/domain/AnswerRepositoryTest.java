package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;
import static qna.domain.QuestionTest.Q1;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @AfterEach
    void clear() {
        answerRepository.deleteAll();
    }

    @Test
    @DisplayName("답변을 저장한다.")
    void save() {
        Answer savedAnswer = answerRepository.save(A1);
        Answer foundAnswer = answerRepository.getOne(savedAnswer.getId());

        assertThat(savedAnswer)
                .isNotNull()
                .isEqualTo(foundAnswer);
    }

    @Test
    @DisplayName("질문Id로 답변 리스트를 조회한다.")
    void findByQuestionIdAndDeletedFalse() {
        Answer savedAnswer1 = answerRepository.save(A1);
        Answer savedAnswer2 = answerRepository.save(A2);
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(Q1.getId());

        assertThat(answers)
                .containsExactly(savedAnswer1, savedAnswer2);
    }

    @Test
    @DisplayName("답변Id로 조회한다.")
    void findByIdAndDeletedFalse() {
        Answer savedAnswer = answerRepository.save(A1);
        Optional<Answer> foundAnswer = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId());

        assertThat(foundAnswer)
                .isNotEmpty()
                .hasValueSatisfying(answer -> assertThat(answer).isEqualTo(savedAnswer));
    }

}
