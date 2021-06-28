package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AnswerTest {

    @Autowired
    AnswerRepository answers;

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void save() {
        Answer actual = answers.save(A1);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getContents()).isEqualTo("Answers Contents1");
    }
}
