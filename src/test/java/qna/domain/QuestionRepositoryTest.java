package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;

    public Question questionTest;

    @BeforeEach
    void setUp() {
        questionTest = QuestionTest.Q1;
    }

    @Test
    @DisplayName("Question 저장한 엔티티의 id로 조회한 경우 동일성 테스트")
    void find() {
        Question saveQuestion = questionRepository.save(questionTest);
        Question findQuestion = questionRepository.findById(saveQuestion.getId()).orElse(null);
        assertThat(saveQuestion).isEqualTo(findQuestion);
    }
}
