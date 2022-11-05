package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest
public class AnswerTest {
    @Autowired
    private AnswerRepository answerRepository;

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void save() {
        assertAll(
                () -> assertDoesNotThrow(() -> answerRepository.save(A1)),
                () -> assertDoesNotThrow(() -> answerRepository.save(A2))
        );
    }
}
