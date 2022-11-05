package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("save 검증 테스트")
    void saveTest() {
        Question question = questionRepository.save(Q1);

        assertAll(
            () -> assertNotNull(question.getId()),
            () -> assertEquals(question.getTitle(), Q1.getTitle()),
            () -> assertEquals(question.getContents(), Q1.getContents()),
            () -> assertEquals(question.getWriterId(), Q1.getWriterId())
        );
    }


    @Test
    @DisplayName("findByDeletedFalse 테스트")
    void findByDeletedFalseTest() {
        questionRepository.save(QuestionTest.Q1);
        questionRepository.save(QuestionTest.Q2);

        List<Question> questions = questionRepository.findByDeletedFalse();

        assertAll(
            () -> assertThat(questions).hasSize(2),
            () -> assertTrue(questions.stream().noneMatch(Question::isDeleted))
        );
    }


    @Test
    @DisplayName("findByIdAndDeletedFalse 테스트")
    void findByIdAndDeletedFalse() {
        Question questionSaved = questionRepository.save(QuestionTest.Q1);
        Question questionFound = questionRepository.findByIdAndDeletedFalse(questionSaved.getId()).get();

        assertFalse(questionFound.isDeleted());
    }
}
