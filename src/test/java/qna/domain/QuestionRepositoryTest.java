package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.UserRepositoryTest.user;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Question question;

    @BeforeEach
    void setUp() {
        user = userRepository.save(user());
        question = questionRepository.save(question(user));
    }

    @Test
    void 질문을_저장한다() {
        assertAll(
                () -> assertThat(question.getTitle()).isEqualTo("title1"),
                () -> assertThat(question.getContents()).isEqualTo("contents1"),
                () -> assertThat(question.getCreatedAt()).isBefore(LocalDateTime.now()),
                () -> assertThat(question.getUpdatedAt()).isBefore(LocalDateTime.now())
        );
    }

    @Test
    void 삭제안된_질문을_아이디로_조회한다() {
        Optional<Question> expected = questionRepository.findByIdAndDeletedFalse(question.getId());
        assertAll(
                () -> assertThat(expected.isPresent()).isTrue(),
                () -> assertThat(expected.get()).isEqualTo(question)
        );
    }

    public static Question question(User user) {
        return new Question("title1", "contents1").writeBy(user);
    }
}