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
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private Question q1;
    private Question q2;

    @BeforeEach
    void setUp() {
        User javajigi = userRepository.save(new User("javajigi", "1234", "javajigi", "a@email.com"));
        User insup = userRepository.save(new User("insup", "1234", "insup", "b@email.com"));

        q1 = questionRepository.save(new Question("title1", "contents1").writeBy(javajigi));
        q2 = questionRepository.save(new Question("title2", "contents2").writeBy(insup));
        q2.delete();
    }

    @Test
    void save() {
        assertAll(
                () -> assertThat(q1.getId()).isNotNull(),
                () -> assertThat(q1.getTitle()).isEqualTo("title1")
        );
    }

    @Test
    void findByIdAndDeletedTrue() {
        Optional<Question> optQuestion = questionRepository.findByIdAndDeletedFalse(q2.getId());

        assertThatThrownBy(() -> optQuestion.orElseThrow(NoSuchElementException::new))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void findByDeletedFalse() {
        List<Question> questions = questionRepository.findAll();

        assertAll(
                () -> assertThat(questions).hasSize(1),
                () -> assertThat(questions).containsExactly(q1)
        );
    }
}
