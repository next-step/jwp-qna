package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        userRepository.save(UserTest.JAVAJIGI);
        questionRepository.saveAll(
                Arrays.asList(QuestionTest.Q1, QuestionTest.Q2, QuestionTest.Q3)
        );
    }

    @Test
    void save() {
        final User javajigi = userRepository.findById(UserTest.JAVAJIGI.getId()).get();
        final Question question = questionRepository.save(new Question("title123", "contents123").writeBy(javajigi));
        assertThat(question.getId()).isNotNull();
        assertThat(question.isOwner(javajigi)).isTrue();
        assertThat(question.getWriter()).isEqualTo(javajigi);
    }

    @Test
    void findByDeletedFalse() {
        QuestionTest.Q1.setDeleted(false);
        QuestionTest.Q2.setDeleted(false);
        QuestionTest.Q3.setDeleted(true);

        List<Question> questions = questionRepository.findByDeletedFalse();
        assertThat(questions).contains(QuestionTest.Q1, QuestionTest.Q2);
        assertThat(questions).doesNotContain(QuestionTest.Q3);
    }

    @Test
    void findByIdAndDeletedFalse() {
        QuestionTest.Q1.setDeleted(false);
        final Question question = questionRepository.findByIdAndDeletedFalse((QuestionTest.Q1.getId())).get();
        assertThat(question).isNotNull();
    }

    @AfterEach
    void after() {
        questionRepository.deleteAll();
    }
}
