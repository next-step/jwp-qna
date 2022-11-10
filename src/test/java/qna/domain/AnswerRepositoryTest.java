package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    private Answer answer;

    @BeforeEach
    void setUp() {
        answer = answerRepository.save(AnswerTest.A2);
    }

    @Test
    void findByQuestionIdAndDeletedFalse() {

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q2.getId());

        assertAll(
                () -> assertThat(answers).isNotNull(),
                () -> assertThat(answers).contains(answer)
        );
    }

    @Test
    void findByIdAndDeletedFalse() {

        Answer findAnswer = answerRepository.findByIdAndDeletedFalse(answer.getId()).orElseGet(null);

        assertAll(
                () -> assertThat(findAnswer).isNotNull(),
                () -> assertThat(findAnswer).isEqualTo(answer)
        );
    }
}