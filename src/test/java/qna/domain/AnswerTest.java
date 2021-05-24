package qna.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import qna.config.TestDataSourceConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@TestDataSourceConfig
public class AnswerTest {

    public static final Answer A1 = new Answer(JAVAJIGI, Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(SANJIGI, Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private Answer answer1;
    private Answer answer2;

    private Question question;

    @BeforeEach
    void setUp() {

        User javajigi = userRepository.save(JAVAJIGI);
        User sanjigi = userRepository.save(SANJIGI);

        question = new Question("title1", "contents1").writeBy(javajigi);
        question = questionRepository.save(question);

        answer1 = new Answer(javajigi, question, "Answers Contents1");
        answer2 = new Answer(sanjigi, question, "Answers Contents2");

        question.setWriter(javajigi);

        answer1.setWriter(javajigi);
        answer2.setWriter(sanjigi);
    }

    @DisplayName("Answer entity 저장 검증")
    @Test
    void saveTest() {
        question.addAnswer(answer1);
        Answer saved = answerRepository.save(answer1);
        assertNotNull(saved.getId());

        assertEquals(answer1.getWriter(), saved.getWriter());
        assertEquals(answer1.getQuestion(), saved.getQuestion());
    }

    @DisplayName("findById 검증")
    @Test
    void findByIdAndDeletedFalseTest() {
        Answer expected = answerRepository.save(answer2);
        Answer actual = answerRepository.findByIdAndDeletedFalse(expected.getId())
                                        .orElseThrow(EntityNotFoundException::new);

        equals(expected, actual);
    }

    private void equals(Answer expected, Answer actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getWriter(), actual.getWriter());
        assertEquals(expected.getQuestion(), actual.getQuestion());
        assertEquals(expected.getContents(), actual.getContents());
    }
}
