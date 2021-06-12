package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionRepositoryTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserRepositoryTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserRepositoryTest.SANJIGI);

    @Autowired
    QuestionRepository questionRepository;

    @Test
    void create() {
        Question question = questionRepository.save(Q1);
        assertThat(question.getId()).isEqualTo(Q1.getId());
    }

    @Test
    void findByDeletedFalse() {
        questionRepository.save(Q1);
        questionRepository.save(Q2);

        List<Question> questions = questionRepository.findByDeletedFalse();
        assertThat(questions.size()).isEqualTo(2);
        assertThat(questions).contains(Q1, Q2);
    }

    @Test
    void findByIdAndDeletedFalse() {
        questionRepository.save(Q1);
        Optional<Question> question = questionRepository.findByIdAndDeletedFalse(Q1.getId());

        assertThat(question.isPresent()).isTrue();
    }
}
