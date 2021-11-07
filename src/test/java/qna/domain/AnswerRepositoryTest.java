package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.QuestionRepositoryTest.question;
import static qna.domain.UserRepositoryTest.user;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void 정답을_저장한다() {
        // given
        User user = userRepository.save(user());
        Question question = questionRepository.save(question(user));

        // when
        Answer answer = answerRepository.save(new Answer(user, question, "contents"));

        // then
        assertAll(
                () -> assertThat(answer.getContents()).isEqualTo("contents"),
                () -> assertThat(answer.getQuestionId()).isEqualTo(question.getId()),
                () -> assertThat(answer.getWriterId()).isEqualTo(user.getId()),
                () -> assertThat(answer.getCreatedAt()).isBefore(LocalDateTime.now()),
                () -> assertThat(answer.getUpdatedAt()).isBefore(LocalDateTime.now()),
                () -> assertThat(answer.isDeleted()).isEqualTo(false)
        );
    }
}