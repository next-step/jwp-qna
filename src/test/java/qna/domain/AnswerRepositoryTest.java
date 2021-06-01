package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answers;

    @Test
    void save() {
        Answer expected = AnswerTest.A1;
        Answer actual = answers.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(actual.getQuestionId()).isEqualTo(expected.getQuestionId()),
                () -> assertThat(actual.getWriterId()).isEqualTo(expected.getWriterId())
        );
    }

    @Test
    void findByName() {
        Answer expected = AnswerTest.A2;
        answers.save(expected);
        Answer actual = answers.findById(expected.getId()).get();
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void update() {
        Answer expected = AnswerTest.A1;
        Answer saved = answers.save(expected);

        saved.setContents("Answer Contents Changed");
        answers.flush();
    }

    @Test
    void delete() {
        Answer expected = AnswerTest.A1;
        Answer saved = answers.save(expected);

        answers.delete(saved);
        answers.flush();
    }

}