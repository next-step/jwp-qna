package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.util.ReflectionTestUtils;
import qna.CannotDeleteException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class QuestionRepositoryTest {
    public static final Question Q1 = new Question("title1", "contents1");
    public static final Question Q2 = new Question("title2", "contents2");

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private Question q1;
    private Question q2;

    @BeforeEach
    void setUp() {
        User javajigi = userRepository.save(UserRepositoryTest.JAVAJIGI);
        User sanjigi = userRepository.save(UserRepositoryTest.SANJIGI);

        Q1.writeBy(javajigi);
        Q2.writeBy(sanjigi);

        q1 = questionRepository.save(Q1);
        q2 = questionRepository.save(Q2);
        q2.deleteQuestion();


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
        List<Question> questions = questionRepository.findAll();

        //then
        assertAll(
                () -> assertThat(questions).hasSize(1),
                () -> assertThat(questions).containsExactly(q1)
        );
    }
}
