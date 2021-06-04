package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questions;
    private Question question;

    @BeforeEach
    void setUp() {
        question = questions.save(QuestionTest.Q1);
    }

    @Test
    void findByDeletedFalse() {
        Question expected = questions.findByDeletedFalse().get(0);
        assertAll(
                () -> assertThat(expected).isNotNull(),
                () -> assertThat(expected.isDeleted()).isFalse()
        );
    }

    @Test
    void findByIdAndDeletedFalse() {
        Question expected = questions.findByIdAndDeletedFalse(question.getId()).get();
        assertAll(
                () -> assertThat(expected).isNotNull(),
                () -> assertThat(expected.isDeleted()).isFalse()
        );
    }
}