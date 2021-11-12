package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Question 테스트")
class QuestionTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("Save 확인")
    @Test
    void save_확인() {
        // given
        User user = userRepository.save(UserTestFactory.create("user"));
        Question question = QuestionTestFactory.create("title", "content", user);

        // when
        Question actual = questionRepository.save(question);

        assertThat(actual)
                .isEqualTo(question);
    }

    @DisplayName("findById 확인")
    @Test
    void findById_확인() {
        // given
        User user = userRepository.save(UserTestFactory.create("user"));
        Question question = QuestionTestFactory.create("title", "contents", user);

        // when
        Question savedQuestion = questionRepository.save(question);
        Optional<Question> actual = questionRepository.findById(savedQuestion.getId());

        // then
        assertThat(actual)
                .isPresent()
                .contains(savedQuestion);
    }

    @DisplayName("update 확인")
    @Test
    void update_확인() {
        // given
        User user = userRepository.save(UserTestFactory.create("user"));
        Question question = QuestionTestFactory.create("title", "contents", user);

        // when
        Question savedQuestion = questionRepository.save(question);
        savedQuestion.setTitle("title2");
        savedQuestion.setContents("contents2");

        Optional<Question> actual = questionRepository.findById(savedQuestion.getId());

        // then
        assertThat(actual)
                .isPresent();

        assertThat(actual.get().getTitle())
                .isEqualTo("title2");

        assertThat(actual.get().getContents())
                .isEqualTo("contents2");
    }
}
