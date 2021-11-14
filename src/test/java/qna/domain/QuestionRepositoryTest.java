package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Question question;
    private LocalDateTime startTime;

    @BeforeEach
    void setUp() {
        startTime = LocalDateTime.now();
        user = new User("userId", "password", "name", "email");
        userRepository.save(user);

        question = questionRepository.save(new Question("title", "contents").writeBy(user));
    }

    @DisplayName("질문 저장")
    @Test
    void save() {
        Question actual = questionRepository.findById(this.question.getId()).get();

        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(question.getId()),
            () -> assertThat(actual.getContents()).isEqualTo(question.getContents()),
            () -> assertThat(actual.getTitle()).isEqualTo(question.getTitle()),
            () -> assertThat(actual.getWriter()).isEqualTo(question.getWriter()),
            () -> assertThat(actual.getCreatedAt()).isAfterOrEqualTo(startTime),
            () -> assertThat(actual.getUpdatedAt()).isAfterOrEqualTo(startTime)
        );

        questionRepository.flush();
    }

    @DisplayName("삭제되지 않은 질문 조회")
    @Test
    void findByDeletedFalse() {
        Question expected1 = new Question("title2", "contents2").writeBy(user);
        Question expected2 = new Question("title3", "contents3").writeBy(user);
        question.setDeleted(true);
        questionRepository.save(expected1);
        questionRepository.save(expected2);
        questionRepository.save(question);

        List<Question> notDeletedQuestions = questionRepository.findByDeletedFalse();

        assertIterableEquals(notDeletedQuestions, Arrays.asList(expected1, expected2));
    }

    @DisplayName("삭제되지 않은 질문 id로 조회")
    @Test
    void findByIdAndDeletedFalse_exists() {
        Question actual = questionRepository.findByIdAndDeletedFalse(question.getId()).get();

        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(question.getId()),
            () -> assertThat(actual.getContents()).isEqualTo(question.getContents()),
            () -> assertThat(actual.getTitle()).isEqualTo(question.getTitle()),
            () -> assertThat(actual.getWriter()).isEqualTo(question.getWriter()),
            () -> assertThat(actual.getCreatedAt()).isAfterOrEqualTo(startTime),
            () -> assertThat(actual.getUpdatedAt()).isAfterOrEqualTo(startTime)
        );
    }

    @DisplayName("삭제되지 않은 질문 id로 조회하여 존재하지 않는 케이스")
    @Test
    void findByIdAndDeletedFalse_notExists() {
        question.setDeleted(true);
        questionRepository.save(question);

        Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(user.getId());

        assertThat(actual.isPresent()).isFalse();
    }

    @DisplayName("작성자로 질문 조회")
    @Test
    void findByWriter() {
        Question actual = questionRepository.findByWriter(user).get();

        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(question.getId()),
            () -> assertThat(actual.getContents()).isEqualTo(question.getContents()),
            () -> assertThat(actual.getTitle()).isEqualTo(question.getTitle()),
            () -> assertThat(actual.getWriter()).isEqualTo(question.getWriter()),
            () -> assertThat(actual.getCreatedAt()).isAfterOrEqualTo(startTime),
            () -> assertThat(actual.getUpdatedAt()).isAfterOrEqualTo(startTime)
        );
    }
}