package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answers;
    private Answer answer;

    @BeforeEach
    void setUp() {
        answer = answers.save(AnswerTest.A1);
    }

    @Test
    void findByQuestionIdAndDeletedFalse() {
        Answer expected = answers.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId()).get(0);
        assertAll(
                () -> assertThat(expected.isDeleted()).isFalse(),
                () -> assertThat(expected.getQuestionId()).isEqualTo(AnswerTest.A1.getQuestionId())
        );
    }

    @Test
    void findByIdAndDeletedFalse() {
        Answer expected = answers.findByIdAndDeletedFalse(answer.getId()).get();
        assertAll(
                () -> assertThat(expected).isNotNull(),
                () -> assertThat(expected.isDeleted()).isFalse()
        );
    }
}