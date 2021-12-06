package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void 질문_저장_후_조회_테스트() {
        Question question = questionRepository.save(QuestionTest.Q1);

        Optional<Question> optionalQuestion = questionRepository.findByIdAndDeletedFalse(question.getId());

        assertThat(optionalQuestion).isNotEmpty();
        assertThat(optionalQuestion.get()).isEqualTo(question);
    }
}
