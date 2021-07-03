package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static org.assertj.core.api.Assertions.assertThat;

@EnableJpaAuditing
@DataJpaTest
public class AnswerTest {

    @Autowired
    AnswerRepository answers;
    @Autowired
    QuestionRepository questions;
    @Autowired
    UserRepository users;

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void save() {
        users.save(UserTest.JAVAJIGI);
        questions.save(QuestionTest.Q1);
        Answer actual = answers.save(A1);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getContents()).isEqualTo("Answers Contents1");
    }
}
