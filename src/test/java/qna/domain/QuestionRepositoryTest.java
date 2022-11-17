package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    private Question question;
    private User user;

    @BeforeEach
    public void setUp() {
        user = userRepository.save(new User(1L, "javajigi", "password", "name", "javajigi@slipp.net"));
        question = questionRepository.save(new Question("titleTest", "contestTest")).writeBy(user);

    }

    @Test
    void findByDeletedFalse() {

        List<Question> questions = questionRepository.findByDeletedFalse();

        assertAll(
                () -> assertThat(questions).isNotNull(),
                () -> assertThat(questions).hasSize(1),
                () -> assertThat(questions).contains(question)
        );
    }

    @Test
    void findByIdAndDeletedFalse() {

        assertThat(question).isNotNull();

        questionRepository.delete(question);

        assertThat(questionRepository.findByIdAndDeletedFalse(question.getId())).isNotPresent();
    }
}