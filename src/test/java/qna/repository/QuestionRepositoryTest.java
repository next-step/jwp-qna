package qna.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;
import qna.domain.QuestionTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void findByDeletedFalse_조회_테스트() {
        Question question = questionRepository.save(QuestionTest.Q1);

        List<Question> results = questionRepository.findByDeletedFalse();

        assertThat(results.get(0)).isSameAs(question);
    }

    @Test
    void findByIdAndDeletedFalse_조회_테스트() {
        Question question = questionRepository.save(QuestionTest.Q1);

        Optional<Question> result = questionRepository.findByIdAndDeletedFalse(question.getId());

        Assertions.assertAll(
                () -> assertThat(result.isPresent()).isTrue(),
                () -> assertThat(result.get()).isSameAs(question)
        );
    }
}