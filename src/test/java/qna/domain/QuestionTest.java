package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    private static Question saved;

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        saved = questionRepository.save(new Question("질문", "행복이란 무엇인가?").writeBy(UserTest.JAVAJIGI));
    }

    @Test
    void save() {
        Question actual = questionRepository.save(Q2);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getTitle()).isEqualTo(Q2.getTitle());
    }

    @Test
    void identity() {
        Question actual = questionRepository.findById(saved.getId()).get();
        assertThat(actual == saved).isTrue();
    }

    @Test
    void update() {
        Question expected = questionRepository.findById(saved.getId()).get();
        expected.setContents("어떻게 살 것인가?");

        Question actual = questionRepository.findById(saved.getId()).get();
        assertThat(actual.getContents()).isEqualTo(expected.getContents());
    }

    @Test
    void delete() {
        questionRepository.deleteById(saved.getId());
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
            () -> questionRepository.findById(saved.getId()).get()
        );
    }

    @Test
    void findByDeletedFalse() {
        List<Question> questions = questionRepository.findByDeletedFalse();
        assertThat(questions).isNotEmpty();
    }

    @Test
    void findByIdAndDeletedFalse() {
        saved.setDeleted(true);
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
            () -> questionRepository.findByIdAndDeletedFalse(saved.getId()).get()
        );
    }
}
