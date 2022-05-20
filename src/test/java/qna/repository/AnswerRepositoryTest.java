package qna.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.AnswerTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void findByQuestionIdAndDeletedFalse_조회_테스트() {
        Answer answer = answerRepository.save(AnswerTest.A1);

        List<Answer> results = answerRepository.findByQuestionIdAndDeletedFalse(answer.getQuestionId());

        assertThat(results.get(0)).isSameAs(answer);
    }

    @Test
    void findByIdAndDeletedFalse_조회_테스트() {
        Answer answer = answerRepository.save(AnswerTest.A1);

        Optional<Answer> result = answerRepository.findByIdAndDeletedFalse(answer.getId());

        Assertions.assertAll(
                () -> assertThat(result.isPresent()).isTrue(),
                () -> assertThat(result.get()).isSameAs(answer)
        );
    }
}