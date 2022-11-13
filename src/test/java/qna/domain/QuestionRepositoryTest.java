package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;

    private User user;

    @BeforeEach
    void init() {
        user = userRepository.save(UserTest.JAVAJIGI);
    }

    @Test
    @DisplayName("Question 저장")
    void question_repository_save() {
        Question question = questionRepository.save(new Question("title1", "contents1").writeBy(user));
        assertThat(question.getId()).isNotNull();
    }

    @Test
    @DisplayName("QuestionRepository Find By Deleted False Test")
    void question_repository_findByDeletedFalse_return() {
        Question question = questionRepository.save(new Question("title1", "contents1").writeBy(user));
        List<Question> questionList = questionRepository.findByDeletedFalse();
        assertAll(
                () -> assertThat(questionList).hasSize(1),
                () -> assertThat(questionList).contains(question)
        );
    }

    @Test
    @DisplayName("QuestionRepository Find By Id And Deleted False Test")
    void question_repository_findByIdAndDeletedFalse_return() {
        Question question = questionRepository.save(new Question("title1", "contents1").writeBy(user));
        Optional<Question> result = questionRepository.findByIdAndDeletedFalse(question.getId());
        assertThat(result.get()).isEqualTo(question);
    }
}
