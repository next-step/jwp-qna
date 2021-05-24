package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import qna.config.TestDataSourceConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@TestDataSourceConfig
public class AnswerTest {
    public static final Answer A1 = new Answer(JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        A1.setWriter(userRepository.save(JAVAJIGI));
        A2.setWriter(userRepository.save(SANJIGI));
    }

    @DisplayName("Answer entity 저장 검증")
    @MethodSource("testcase")
    @ParameterizedTest
    void saveTest(Answer answer) {
        userRepository.save(answer.getWriter());

        Answer saved = answerRepository.save(answer);
        assertNotNull(saved.getId());

        assertEquals(answer.getWriter(), saved.getWriter());
        assertEquals(answer.getQuestionId(), saved.getQuestionId());
    }

    @DisplayName("findById 검증")
    @MethodSource("testcase")
    @ParameterizedTest
    void findByIdAndDeletedFalseTest(Answer answer) {
        userRepository.save(answer.getWriter());
        Answer expected = answerRepository.save(answer);
        Answer actual = answerRepository.findByIdAndDeletedFalse(expected.getId())
                                        .orElseThrow(EntityNotFoundException::new);

        equals(expected, actual);
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> testcase() {
        return Stream.of(Arguments.of(A1), Arguments.of(A2));
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
        assertEquals(expected.getWriter(), actual.getWriter());
        assertEquals(expected.getQuestionId(), actual.getQuestionId());
        assertEquals(expected.getContents(), actual.getContents());
    }
}
