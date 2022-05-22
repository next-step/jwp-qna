package qna.domain;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answerRepository;

    @BeforeEach
    void before() {
        answerRepository.deleteAll();
    }

    @DisplayName("Answer 저장 테스트")
    @Test
    void saveWithBaseTimeEntityTest() {
        Answer expected = AnswerTest.A1;
        Answer result = answerRepository.save(expected);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("Answer 조회 테스트 / Id로 조회")
    @Test
    void findTest01() {
        Answer expected = answerRepository.save(AnswerTest.A1);
        Optional<Answer> resultOptional = answerRepository.findByIdAndDeletedFalse(expected.getId());
        assertThat(resultOptional).isNotEmpty();

        Answer result = resultOptional.get();
        assertThat(result.getId()).isNotNull();
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("Answer 조회 테스트 / QuestionId로 조회")
    @Test
    void findTest02() {
        Answer expected = answerRepository.save(AnswerTest.A1);
        List<Answer> results = answerRepository.findByQuestionIdAndDeletedFalse(expected.getQuestionId());
        assertThat(results)
                .hasSize(1)
                .contains(expected);

        Answer expected2 = answerRepository.save(AnswerTest.A2);
        results = answerRepository.findByQuestionIdAndDeletedFalse(expected.getQuestionId());
        assertThat(results)
                .hasSize(2)
                .contains(expected, expected2);
    }
}