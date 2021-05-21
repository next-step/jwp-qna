package qna.domain;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import qna.config.AuditConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(AuditConfiguration.class)
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @DisplayName("Answer entity 저장 검증")
    @Test
    void saveTest() {
        Answer saved = answerRepository.save(A1);
        assertNotNull(saved.getId());

        assertEquals(A1.getWriterId(), saved.getWriterId());
        assertEquals(A1.getQuestionId(), saved.getQuestionId());
    }

    @DisplayName("findById 검증")
    @Test
    void findByIdAndDeletedFalseTest() {
        Answer expected = answerRepository.save(A2);
        Answer actual = answerRepository.findByIdAndDeletedFalse(expected.getId())
                                        .orElseThrow(IllegalArgumentException::new);

        equals(expected, actual);
    }

    @DisplayName("findByQuestionIdAndDeletedFalse 검증")
    @Test
    void findByQuestionIdAndDeletedFalseTest() {

        List<Answer> expected = new ArrayList<>();
        expected.add(answerRepository.save(A1));
        expected.add(answerRepository.save(A2));

        List<Answer> actual = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());

        for (int i = 0; i < expected.size(); i++) {
            equals(expected.get(i), actual.get(i));
        }
    }

    private void equals(Answer expected, Answer actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getWriterId(), actual.getWriterId());
        assertEquals(expected.getQuestionId(), actual.getQuestionId());
        assertEquals(expected.getContents(), actual.getContents());
    }
}
