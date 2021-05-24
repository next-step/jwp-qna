package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    AnswerRepository answerRepository;

    @AfterEach
    public void clear() {
        answerRepository.deleteAll();
    }

    @Test
    void save() {
        Answer a1 = answerRepository.save(A1);

        answerRepository.findById(a1.getId())
                .orElseThrow(IllegalArgumentException::new);

        assertThat(answerRepository.findById(a1.getId()).get()).isEqualTo(a1);
    }
}
