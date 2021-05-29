package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    private Question q1;
    private Question q2;

    @BeforeEach
    void setUp() {
        q1 = questionRepository.save(Q1);
        q2 = questionRepository.save(Q2);
        q2.setDeleted(true);
    }

    @Test
    void save() {
        //then
        assertAll(
                () -> assertThat(q1.getId()).isNotNull(),
                () -> assertThat(q1.getTitle()).isEqualTo("title1")
        );
    }

    @Test
    void findByIdAndDeletedTrue() {
        //when, then
        Optional<Question> optQuestion = questionRepository.findByIdAndDeletedFalse(q2.getId());

        //then
        assertThatThrownBy(() -> optQuestion.orElseThrow(NoSuchElementException::new))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void findByDeletedFalse() {
        //when
        List<Question> questions = questionRepository.findByDeletedFalse();

        //then
        assertAll(
                () -> assertThat(questions).hasSize(1),
                () -> assertThat(questions).containsExactly(q1)
        );
    }
}
