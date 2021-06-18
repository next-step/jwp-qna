package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answers;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        answers.deleteAll();
    }

    @Test
    void saveTests(){
        Answer expected = AnswerTest.A1;
        Answer actual = answers.save(AnswerTest.A1);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    void findByIdAndDeletedFalseTest(){

        Answer answerNotDeletedResult = answers.save(AnswerTest.A1);
        Answer answerDeletedResult = answers.save(AnswerTest.A2);
        Optional<Answer> answerNotDeletedActual = answers.findByIdAndDeletedFalse(answerNotDeletedResult.getId());
        Optional<Answer> answerDeletedActual = answers.findByIdAndDeletedFalse(answerDeletedResult.getId());

        assertAll(
                () -> assertThat(answerDeletedActual).isEmpty(),
                () -> assertThat(answerNotDeletedActual).isPresent()
        );
    }
}