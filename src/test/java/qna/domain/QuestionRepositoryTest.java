package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    private Question question;

    @BeforeEach
    public void setUp() {
        question = new Question("titleTest", "contestTest");
    }

    @Test
    void findByDeletedFalse() {
        question = questionRepository.save(QuestionTest.Q1);

        List<Question> questions = questionRepository.findByDeletedFalse();

        assertAll(
                () -> assertThat(questions).isNotNull(),
                () -> assertThat(questions).hasSize(1),
                () -> assertThat(questions).contains(question)
        );
    }

    @Test
    void findByIdAndDeletedFalse() {

        assertThat(question).isNotNull();

        questionRepository.delete(question);

        assertThat(questionRepository.findByIdAndDeletedFalse(question.getId())).isNotPresent();
    }
}