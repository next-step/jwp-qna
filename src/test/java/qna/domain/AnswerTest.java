package qna.domain;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1,
        "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1,
        "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("save 검증 테스트")
    void saveTest() {
        Answer answer = answerRepository.save(A1);

        assertAll(
            () -> assertNotNull(answer.getId()),
            () -> assertEquals(answer.getWriterId(), A1.getWriterId()),
            () -> assertEquals(answer.getQuestionId(), A1.getQuestionId())
        );
    }

    @Test
    @DisplayName("findByQuestionIdAndDeletedFalse 검증 테스트")
    void findByQuestionIdAndDeletedFalseTest() {
        answerRepository.save(AnswerTest.A1);

        List<Answer> answersFound =
            answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());

        assertAll(
            () -> assertThat(answersFound).hasSize(1),
            () -> assertTrue(answersFound.stream().noneMatch(Answer::isDeleted))
        );
    }

    @Test
    @DisplayName("findByIdAndDeletedFalse 검증 테스트")
    void findByIdAndDeletedFalseTest() {
        Answer answerSaved = answerRepository.save(AnswerTest.A1);
        Answer answerFound = answerRepository.findByIdAndDeletedFalse(answerSaved.getId()).get();

        assertFalse(answerFound.isDeleted());
    }

}
