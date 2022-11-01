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
    @DisplayName("User 저장 테스트")
    void save() {
        Question question = questionRepository.save(questionTest);
        assertThat(question.getId()).isNotNull();
    }
}
