package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    private Question question1;
    private Question question2;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(new User(1L, "javajigi", "password", "name", "javajigi@slipp.net"));
        user2 = userRepository.save(new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net"));

        question1 = questionRepository.save(new Question("title1", "contents1").writeBy(user1));
        question2 = questionRepository.save(new Question("title2", "contents2").writeBy(user2));
    }

    @Test
    void save() {
        assertThat(question1.getWriter()).isEqualTo(user1);
    }

    @Test
    void findByDeletedFalse() {
        List<Question> questions = questionRepository.findByDeletedFalse();
        assertThat(questions.size()).isEqualTo(2);
        assertThat(questions).contains(question1, question2);
    }

    @Test
    void findByIdAndDeletedFalse() {
        questionRepository.delete(question2);
        Optional<Question> existQuestion = questionRepository.findByIdAndDeletedFalse(question1.getId());
        Optional<Question> deletedQuestion = questionRepository.findByIdAndDeletedFalse(question2.getId());

        assertThat(existQuestion.isPresent()).isTrue();
        assertThat(deletedQuestion.isPresent()).isFalse();
    }

    @Test
    void update() {
        question1.setDeleted(true);
        questionRepository.save(question1);
        assertThat(question1.isDeleted()).isTrue();
    }

    @Test
    void delete() {
        questionRepository.delete(question1);
        Optional<Question> expected = questionRepository.findById(question1.getId());
        assertThat(expected.isPresent()).isFalse();
    }
}
