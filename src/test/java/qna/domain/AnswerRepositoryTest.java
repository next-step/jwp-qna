package qna.domain;

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

    private Answer answerDeleted;
    private Answer answerNotDeleted;

    private Answer answerDeletedResult;
    private Answer answerNotDeletedResult;

    @BeforeEach
    void setUp() {
        answerNotDeletedResult = answers.save(AnswerTest.A1);
        answerDeletedResult = answers.save(AnswerTest.A2);
    }

    @Test
    void saveTests(){
        assertAll(
                () -> assertThat(answerNotDeletedResult.getId()).isNotNull(),
                () -> assertThat(answerNotDeletedResult.getContents()).isEqualTo(AnswerTest.A1.getContents())
        );
    }

    @Test
    void findByIdAndDeletedFalseTest(){


        Optional<Answer> answerNotDeletedActual = answers.findByIdAndDeletedFalse(answerNotDeletedResult.getId());
        Optional<Answer> answerDeletedActual = answers.findByIdAndDeletedFalse(answerDeletedResult.getId());

        assertAll(
                () -> assertThat(answerDeletedActual).isEmpty(),
                () -> assertThat(answerNotDeletedActual).isPresent()
        );
    }
}