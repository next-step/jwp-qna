package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
//        userRepository.save(UserTest.JAVAJIGI);
//        questionRepository.saveAll(
//                Arrays.asList(QuestionTest.Q1, QuestionTest.Q2, QuestionTest.Q3)
//        );
    }

    @Test
    void save() {
        final User javajigi = userRepository.save(UserTest.JAVAJIGI);
        final Question question = questionRepository.save(new Question("title123", "contents123").writeBy(javajigi));
        assertThat(question.getId()).isNotNull();
        assertThat(question.isOwner(javajigi)).isTrue();
        assertThat(question.getWriter()).isEqualTo(javajigi);
    }

    @Test
    void findByDeletedFalse() {
        final User javajigi = userRepository.save(UserTest.JAVAJIGI);
        final User yong = userRepository.save(UserTest.YONG);

        questionRepository.save(QuestionTest.Q1.writeBy(javajigi));
        questionRepository.save(QuestionTest.Q2.writeBy(yong));

        QuestionTest.Q1.setDeleted(false);
        QuestionTest.Q2.setDeleted(true);

        List<Question> questions = questionRepository.findByDeletedFalse();
        assertThat(questions).contains(QuestionTest.Q1);
        assertThat(questions).doesNotContain(QuestionTest.Q2);
    }

    @Test
    void findByIdAndDeletedFalse() {
        final User javajigi = userRepository.save(UserTest.JAVAJIGI);
        questionRepository.save(QuestionTest.Q1.writeBy(javajigi));

        QuestionTest.Q1.setDeleted(false);

        final Question question = questionRepository.findByIdAndDeletedFalse((QuestionTest.Q1.getId())).get();
        assertThat(question).isNotNull();
    }
}
