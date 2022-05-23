package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answers;

    @Test
    void save() {
        answers.save(AnswerTest.A1);
        answers.save(AnswerTest.A2);
        List<Answer> answersAll = answers.findAll();
        assertThat(answersAll).hasSize(2);
    }

    @Test
    void findById() {
        Answer answer = answers.save(AnswerTest.A1);
        Answer expected = answers.findById(1L).get();

        assertThat(expected).isSameAs(answer);
    }

    @Test
    void findByQuestionIdAndDeletedFalse() {
        answers.save(AnswerTest.A1);
        List<Answer> list = answers.findByQuestionIdAndDeletedFalse(AnswerTest.A1.getQuestionId());
        assertThat(list).hasSize(1);
    }
}