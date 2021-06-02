package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answers;

    @Test
    void save() {
        answers.save(A1);
        Answer expected = answers.findAll().get(0);

        System.out.println(">>expected :"+expected);
        assertThat(expected).isNotNull();

        System.out.println(">>expected.getId() :"+expected.getId());
        assertThat(expected.getId()).isNotNull();
    }

    @Test
    void isOwner() {
        answers.save(A1);
        answers.save(A2);
        Answer expected1 = answers.findAll().get(0);
        Answer expected2 = answers.findAll().get(1);

        assertThat(expected1.isOwner(UserTest.JAVAJIGI)).isTrue();
        assertThat(expected2.isOwner(UserTest.SANJIGI)).isTrue();
    }
}
