package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;
import qna.domain.User;
import qna.domain.UserTest;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("질문을 저장 후 확인")
    @Test
    void save() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(new Question("title", "contents").writeBy(user));

        Optional<Question> result = questionRepository.findById(question.getId());

        assertThat(result).get().isEqualTo(question);
    }

    @DisplayName("질문을 저장 후 조회 확인")
    @Test
    void findAll() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        Question question1 = questionRepository.save(new Question("title", "contents").writeBy(user));
        Question question2 = questionRepository.save(new Question("title", "contents").writeBy(user));

        List<Question> result = questionRepository.findAll();

        assertThat(result).hasSize(2)
                .containsExactly(question1, question2);
    }

    @DisplayName("질문을 저장 후 수정 확인")
    @Test
    void update() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(new Question("title", "contents").writeBy(user));
        question.setTitle("update title");

        Optional<Question> result = questionRepository.findById(question.getId());

        assertAll(
            () -> assertThat(result).isPresent(),
            () -> assertThat(result.get().getTitle()).isEqualTo(question.getTitle())
        );
    }

    @DisplayName("질문 저장 후 삭제 확인")
    @Test
    void remove() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(new Question("title", "contents").writeBy(user));
        questionRepository.delete(question);

        Optional<Question> result = questionRepository.findById(question.getId());

        assertThat(result).isNotPresent();
    }

    @DisplayName("삭제되지 않은 질문 조회")
    @Test
    void findByDeletedFalse() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(new Question("title", "contents").writeBy(user));

        List<Question> result = questionRepository.findByDeletedFalse();

        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result).contains(question),
            () -> assertThat(result.get(0)).isEqualTo(question)
        );
    }

    @DisplayName("질문 식별자로 삭제되지 않은 질문 조회")
    @Test
    void findByIdAndDeletedFalse() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(new Question("title", "contents").writeBy(user));

        Optional<Question> result = questionRepository.findByIdAndDeletedFalse(question.getId());

        assertAll(
            () -> assertThat(result.isPresent()).isTrue(),
            () -> assertThat(result.get()).isEqualTo(question)
        );
    }
}