package qna.domain;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @DisplayName("save 검증 성공")
    @Test
    void saveTest() {
        Answer savedAnswer = answerRepository.save(A1);

        assertAll(
                () -> assertNotNull(savedAnswer.getId()),
                () -> assertEquals(savedAnswer.getWriterId(), A1.getWriterId()),
                () -> assertEquals(savedAnswer.getQuestionId(), A1.getQuestionId())
        );
    }

    @DisplayName("findByQuestionIdAndDeletedFalse 검증 성공")
    @Test
    void findByQuestionIdAndDeletedFalseTest() {
        List<Answer> expectedAnswers = new ArrayList<>();
        expectedAnswers.add(answerRepository.save(A1));
        expectedAnswers.add(answerRepository.save(A2));

        List<Answer> actualAnswers = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());

        for (int i = 0; i < expectedAnswers.size(); i++) {
            isEquals(actualAnswers.get(i), expectedAnswers.get(i));
        }
    }

    @DisplayName("findByIdAndDeletedFalseTest 검증 성공")
    @Test
    void findByIdAndDeletedFalseTest() {
        Answer expectedAnswer = answerRepository.save(A2);

        Answer actualAnswer = answerRepository.findByIdAndDeletedFalse(expectedAnswer.getId())
                .orElseThrow(IllegalArgumentException::new);

        isEquals(actualAnswer, expectedAnswer);
    }

    private void isEquals(final Answer actualAnswer, final Answer expectedAnswer) {
        assertAll(
                () -> assertEquals(actualAnswer.getId(), expectedAnswer.getId()),
                () -> assertEquals(actualAnswer.getContents(), expectedAnswer.getContents()),
                () -> assertEquals(actualAnswer.getQuestionId(), expectedAnswer.getQuestionId()),
                () -> assertEquals(actualAnswer.getWriterId(), expectedAnswer.getWriterId())
        );
    }
}
