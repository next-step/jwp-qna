package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

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

    private User user;
    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp() {
        user = userRepository.save(user());
        question = questionRepository.save(question(user));
        answer = answerRepository.save(new Answer(user, question, "contents"));
    }

    @Test
    void 정답을_저장한다() {
        assertAll(
                () -> assertThat(answer.getContents()).isEqualTo("contents"),
                () -> assertThat(answer.getQuestion()).isEqualTo(question),
                () -> assertThat(answer.getWriter()).isEqualTo(user),
                () -> assertThat(answer.getCreatedAt()).isBefore(LocalDateTime.now()),
                () -> assertThat(answer.getUpdatedAt()).isBefore(LocalDateTime.now()),
                () -> assertThat(answer.isDeleted()).isEqualTo(false)
        );
    }

    @Test
    void 정답을_조회한다() {
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());
        assertAll(
                () -> assertThat(answers.size()).isEqualTo(1),
                () -> assertThat(answers.get(0)).isEqualTo(answer)
        );
    }
}