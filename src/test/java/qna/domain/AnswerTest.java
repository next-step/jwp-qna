package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    private Answer a1;
    private Answer a2;

    @BeforeEach
    void setUp() {
        a1 = answerRepository.save(A1);
        a2 = answerRepository.save(A2);
    }

    @Test
    void save() {
        assertAll(
                () -> assertThat(a1.getId()).isNotNull(),
                () -> assertThat(a1.getContents()).isEqualTo("Answers Contents1")
        );
    }

    @Test
    void findById() {
        Answer findAnswer = answerRepository.findById(
                a1.getId()).orElseThrow(() -> new NoSuchElementException()
        );

        assertAll(
                () -> assertThat(findAnswer.getContents()).isEqualTo("Answers Contents1"),
                () -> assertThat(findAnswer.getQuestionId()).isEqualTo(QuestionTest.Q1.getId()),
                () -> assertThat(findAnswer.getWriterId()).isEqualTo(UserTest.JAVAJIGI.getId())
        );

    }
//
//    @Test
//    void update() {
//        a1.setContents("Answers updatedContents1");
//
//        assertAll(
//                () -> assertThat(a1.getContents()).isEqualTo("Answers updatedContents1")
//        );
//    }

    @Test
    void checkNonNull() {
        assertAll(
                () -> assertThat(a1.isDeleted()).isNotNull()
        );
    }
}
