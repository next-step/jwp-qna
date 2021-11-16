package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answers;
    @Autowired
    private QuestionRepository questions;

    @Test
    void test() {
    }

    @Test
    void save() {
        final Answer expected = AnswerTest.A1;
        final Answer actual = answers.save(expected);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    void findByContentsContainingTest() {
        String expected = AnswerTest.A1.getContents();
        answers.save(new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, expected));
        String actual = answers.findByContentsContaining(expected).getContents();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getQuestionTest() {
        answers.save(AnswerTest.A1);
        Answer A1 = answers.findByContentsContaining(AnswerTest.A1.getContents());

        assertThat(A1.getQuestion()).isEqualTo(QuestionTest.Q1);
    }
}
