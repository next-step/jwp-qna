package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.UserRepositoryTest.user;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 질문을_저장한다() {
        // given
        User user = userRepository.save(user());

        // when
        Question expected = questionRepository.save(question(user));

        // then
        assertAll(
                () -> assertThat(expected.getTitle()).isEqualTo("title1"),
                () -> assertThat(expected.getContents()).isEqualTo("contents1"),
                () -> assertThat(expected.getCreatedAt()).isBefore(LocalDateTime.now()),
                () -> assertThat(expected.getUpdatedAt()).isBefore(LocalDateTime.now())
        );
    }

    public static Question question(User user) {
        return new Question("title1", "contents1").writeBy(user);
    }
}