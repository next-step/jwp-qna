package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionRepositoryTest {
    private static final Question questionTest = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void init() {
        questionRepository.save(questionTest);
    }

    @Test
    void findByDeletedFalse() {
        List<Question> questions = questionRepository.findByDeletedFalse();
        assertAll(
                () -> assertThat(questions).isNotEmpty(),
                () -> assertThat(questions.get(0).getTitle()).isEqualTo("title1"),
                () -> assertThat(questions.get(0).getContents()).isEqualTo("contents1"),
                () -> assertThat(questions.get(0).getWriterId()).isEqualTo(1L)
        );
    }

    @Test
    void findByIdAndDeletedFalse() {
        Question question = questionRepository.findByIdAndDeletedFalse(1L).get();
        assertAll(
                () -> assertThat(question).isNotNull(),
                () -> assertThat(question.getTitle()).isEqualTo("title1"),
                () -> assertThat(question.getContents()).isEqualTo("contents1"),
                () -> assertThat(question.getWriterId()).isEqualTo(1L)
        );
    }
}
