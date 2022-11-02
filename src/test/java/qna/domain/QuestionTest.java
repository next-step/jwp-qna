package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("save 검증 성공")
    @Test
    void saveTest() {
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
        expectedQuestions.add(questionRepository.save(Q1));
        expectedQuestions.add(questionRepository.save(Q2));

        List<Question> actualQuestions = questionRepository.findByDeletedFalse();

        for (int i = 0; i < expectedQuestions.size(); i++) {
            isEquals(actualQuestions.get(i), expectedQuestions.get(i));
        }
    }

    @DisplayName("findByIdAndDeletedFalse 검증 성공")
    @Test
    void findByIdAndDeletedFalse() {
        Question expectedQuestion = questionRepository.save(Q1);

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
