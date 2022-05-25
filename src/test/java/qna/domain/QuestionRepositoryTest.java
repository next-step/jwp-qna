package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    @BeforeEach
    void before() {
        questionRepository.deleteAll();
    }

    @DisplayName("Question 저장 테스트")
    @Test
    void saveTest() {
        Question expected = QuestionTest.Q1;
        Question result = questionRepository.save(expected);

        assertThat(result.getId()).isNotNull();
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("Question 조회 테스트 / id로 조회")
    @Test
    void findTest() {
        Question expected = questionRepository.save(QuestionTest.Q1);
        Optional<Question> resultOptional = questionRepository.findByIdAndDeletedFalse(expected.getId());
        assertThat(resultOptional).isNotEmpty();

        Question result = resultOptional.get();

        assertThat(result.getId()).isNotNull();
        assertThat(result).isEqualTo(expected);
    }
}