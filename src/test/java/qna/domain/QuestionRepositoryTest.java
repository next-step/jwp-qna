package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;
    
    @BeforeEach
    void setUp() {
        questionRepository.deleteAllInBatch();
    }
    
    @Test
    void save() {
        Question questionTest = QuestionTest.Q1;
        Question question = questionRepository.save(questionTest);
        assertAll(
                () -> assertThat(question.getId()).isNotNull(),
                () -> assertThat(question.getContents()).isEqualTo(questionTest.getContents()),
                () -> assertThat(question.getWriterId()).isEqualTo(questionTest.getWriterId()),
                () -> assertThat(question.getTitle()).isEqualTo(questionTest.getTitle()),
                () -> assertThat(question.isDeleted()).isEqualTo(questionTest.isDeleted())
        );
    }
}
