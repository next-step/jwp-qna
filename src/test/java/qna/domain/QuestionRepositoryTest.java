package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
    }

    @Test
    void 질문_저장() {
        Question question = new Question("title", "contents").writeBy(user);
        Question actual = questionRepository.save(question);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(question.getContents()),
                () -> assertThat(actual.getWriter()).isEqualTo(user),
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
                () -> assertThat(actual.getWriter()).isEqualTo(user),
                () -> assertThat(actual.getTitle()).isEqualTo(question.getTitle()),
                () -> assertThat(actual.getCreatedAt()).isNotNull(),
                () -> assertThat(actual.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    void 질문_삭제() {
        Question question = questionRepository.save(new Question("title", "contents").writeBy(user));
        questionRepository.deleteById(question.getId());
        assertThat(questionRepository.findByIdAndDeletedFalse(question.getId())).isNotPresent();
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
        questionRepository.save(new Question("title2", "contents2").writeBy(user));
        List<Question> actual = questionRepository.findByDeletedFalse();
        assertThat(actual).hasSize(2);
    }

    @Test
    void 질문_영속성_초기화후_같은객채_조회() {
        Question question = questionRepository.save(new Question("title", "contents").writeBy(user));
        flushAndClear();
        Question actual = questionRepository.findById(question.getId()).get();
        assertThat(actual).isEqualTo(question);
    }

    private void flushAndClear() {
        testEntityManager.flush();
        testEntityManager.clear();
    }
}
