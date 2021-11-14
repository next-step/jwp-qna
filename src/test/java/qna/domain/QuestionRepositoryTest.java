package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionRepositoryTest {

    private Question question;

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    }

    @Test
    void save() {
        final Question actual = questionRepository.save(question);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getTitle()).isNotNull(),
            () -> assertThat(actual.getContents()).isNotNull()
        );
    }

    @Test
    void findByDeletedFalse() {
        question.setDeleted(false);
        questionRepository.save(question);
        final List<Question> actual = questionRepository.findByDeletedFalse();
        assertThat(actual).hasSize(1);
    }

    @Test
    void findByIdAndDeletedFalse() {
        question.setDeleted(false);
        questionRepository.save(question);
        final Question actual = questionRepository.findByIdAndDeletedFalse(question.getId())
            .orElseThrow(NoSuchElementException::new);
        assertThat(actual).isEqualTo(question);
    }
}
