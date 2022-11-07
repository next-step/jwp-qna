package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static qna.fixture.TestFixture.JAVAJIGI;
import static qna.fixture.TestFixture.Q1;

@DataJpaTest
public class QuestionRepositoryTest {

    private Question question1;
    private Question question2;
    private User user;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = userRepository.save(JAVAJIGI);
        question1 = new Question(1L, "title1", "contents1");
        question2 = new Question(2L, "title2", "contents2");
        question1.writeBy(user);
        question2.writeBy(user);
        question1 = questionRepository.save(question1);
        question2 = questionRepository.save(question2);
    }

    @DisplayName("save 검증 성공")
    @Test
    void saveTest() {
        Q1.writeBy(user);
        Question savedQuestion = questionRepository.save(Q1);

        assertAll(
                () -> assertNotNull(savedQuestion.getId()),
                () -> assertEquals(savedQuestion.getTitle(), Q1.getTitle()),
                () -> assertEquals(savedQuestion.getContents(), Q1.getContents()),
                () -> assertEquals(savedQuestion.getWriterId(), Q1.getWriterId())
        );
    }

    @DisplayName("findByDeletedFalse 검증 성공")
    @Test
    void findByDeletedFalse() {
        List<Question> expectedQuestions = new ArrayList<>();
        expectedQuestions.add(question1);
        expectedQuestions.add(question2);

        List<Question> actualQuestions = questionRepository.findByDeletedFalse();

        for (int i = 0; i < expectedQuestions.size(); i++) {
            isEquals(actualQuestions.get(i), expectedQuestions.get(i));
        }
    }

    @DisplayName("findByIdAndDeletedFalse 검증 성공")
    @Test
    void findByIdAndDeletedFalse() {
        Question expectedQuestion = questionRepository.save(question1);

        Question actualQuestion = questionRepository.findByIdAndDeletedFalse(expectedQuestion.getId())
                .orElseThrow(IllegalArgumentException::new);

        isEquals(actualQuestion, expectedQuestion);
    }

    private void isEquals(final Question actualQuestion, final Question expectedQuestion) {
        assertAll(
                () -> assertEquals(actualQuestion.getId(), expectedQuestion.getId()),
                () -> assertEquals(actualQuestion.getTitle(), expectedQuestion.getTitle()),
                () -> assertEquals(actualQuestion.getContents(), expectedQuestion.getContents()),
                () -> assertEquals(actualQuestion.getWriterId(), expectedQuestion.getWriterId())
        );
    }
}
