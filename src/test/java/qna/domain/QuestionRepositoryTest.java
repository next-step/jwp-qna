package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("giraffelim", "password", "sun", "email"));
    }

    @Test
    void 질문_저장() {
        Question question = new Question("title", "contents").writeBy(user);
        Question actual = questionRepository.save(question);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(question.getContents()),
                () -> assertThat(actual.isOwner(user)).isTrue(),
                () -> assertThat(actual.getTitle()).isEqualTo(question.getTitle()),
                () -> assertThat(actual.getCreatedAt()).isNotNull(),
                () -> assertThat(actual.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    void 질문_조회() {
        Question question = questionRepository.save(new Question("title", "contents").writeBy(user));
        Question actual = questionRepository.findById(question.getId()).get();
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(question.getContents()),
                () -> assertThat(actual.isOwner(user)).isTrue(),
                () -> assertThat(actual.getTitle()).isEqualTo(question.getTitle()),
                () -> assertThat(actual.getCreatedAt()).isNotNull(),
                () -> assertThat(actual.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    void repository_의_delete_를_사용해_질문을_삭제_할_경우_예외가_발생() {
        Question question = questionRepository.save(new Question("title", "contents").writeBy(user));
        assertThatThrownBy(() -> questionRepository.deleteById(question.getId()))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("해당 메소드를 사용해 질문을 삭제할 수 없습니다.");
    }

    @Test
    void 삭제되지_않은_질문_조회() {
        Question question = questionRepository.save(new Question("title", "contents").writeBy(user));
        Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(question.getId());
        assertThat(actual).isPresent();
    }

    @Test
    void 삭제되지_않은_질문_목록_조회() {
        questionRepository.save(new Question("title", "contents").writeBy(user));
        questionRepository.save(new Question("question", "answer").writeBy(user));
        List<Question> actual = questionRepository.findByDeletedFalse();
        assertThat(actual).hasSize(2);
    }
}
