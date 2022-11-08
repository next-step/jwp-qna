package qna.domain;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static qna.fixture.TestFixture.JAVAJIGI;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    private Question question;
    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(JAVAJIGI);
        question = new Question(1L, "title", "contents");
        question = questionRepository.save(question);
    }


    @DisplayName("save 검증 성공")
    @Test
    void saveTest() {
        Answer answer = new Answer(user, question, "contents");
        Answer savedAnswer = answerRepository.save(answer);

        assertAll(
                () -> assertNotNull(savedAnswer.getId()),
                () -> assertEquals(savedAnswer.getWriterId(), answer.getWriterId()),
                () -> assertEquals(savedAnswer.getQuestion(), answer.getQuestion())
        );
    }

    @DisplayName("findByQuestionIdAndDeletedFalse 검증 성공")
    @Test
    void findByQuestionIdAndDeletedFalseTest() {
        Answer answer1 = new Answer(user, question, "contents1");
        Answer answer2 = new Answer(user, question, "contents2");
        List<Answer> expectedAnswers = new ArrayList<>();
        expectedAnswers.add(answerRepository.save(answer1));
        expectedAnswers.add(answerRepository.save(answer2));

        List<Answer> actualAnswers = answerRepository.findByQuestionAndDeletedFalse(question);

        for (int i = 0; i < expectedAnswers.size(); i++) {
            isEquals(actualAnswers.get(i), expectedAnswers.get(i));
        }
    }

    @DisplayName("findByIdAndDeletedFalseTest 검증 성공")
    @Test
    void findByIdAndDeletedFalseTest() {
        Answer answer = new Answer(user, question, "contents");
        Answer expectedAnswer = answerRepository.save(answer);

        Answer actualAnswer = answerRepository.findByIdAndDeletedFalse(expectedAnswer.getId())
                .orElseThrow(IllegalArgumentException::new);

        isEquals(actualAnswer, expectedAnswer);
    }

    private void isEquals(final Answer actualAnswer, final Answer expectedAnswer) {
        assertAll(
                () -> assertEquals(actualAnswer.getId(), expectedAnswer.getId()),
                () -> assertEquals(actualAnswer.getContents(), expectedAnswer.getContents()),
                () -> assertEquals(actualAnswer.getQuestion(), expectedAnswer.getQuestion()),
                () -> assertEquals(actualAnswer.getWriterId(), expectedAnswer.getWriterId())
        );
    }
}
