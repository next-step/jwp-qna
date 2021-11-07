package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "userId", "password", "name", "email");
    }

    @Test
    void save() {
        Question expected = new Question("title", "contents").writeBy(user);

        Question actual = questionRepository.save(expected);
        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
            () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
            () -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle()),
            () -> assertThat(actual.getWriterId()).isEqualTo(expected.getWriterId())
        );
    }

    @Test
    void findByDeletedFalse() {
        Question question1 = new Question("title1", "contents1").writeBy(user);
        Question question2 = new Question("title2", "contents2").writeBy(user);
        Question question3 = new Question("title3", "contents3").writeBy(user);
        question3.setDeleted(true);

        questionRepository.save(question1);
        questionRepository.save(question2);
        questionRepository.save(question3);

        List<Question> notDeletedQuestions = questionRepository.findByDeletedFalse();

        assertIterableEquals(notDeletedQuestions, Arrays.asList(question1, question2));
    }

    @Test
    void findByIdAndDeletedFalse_exists() {
        Question expected = new Question("title", "contents").writeBy(user);
        questionRepository.save(expected);

        Question actual = questionRepository.findByIdAndDeletedFalse(1L).get();

        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
            () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
            () -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle()),
            () -> assertThat(actual.getWriterId()).isEqualTo(expected.getWriterId())
        );
    }

    @Test
    void findByIdAndDeletedFalse_notExists() {
        Question expected = new Question("title", "contents").writeBy(user);
        questionRepository.save(expected);

        expected.setDeleted(true);

        Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(1L);

        assertThat(actual.isPresent()).isFalse();
    }
}