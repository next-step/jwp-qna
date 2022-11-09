package qna.repository;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.domain.AnswerTest;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.QuestionTest;
import qna.domain.User;
import qna.domain.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AnswerRepositoryTest {

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
    void setup() {
        user = userRepository.save(new User("iamsojung", "password", "sojung", "email@gmail.com"));
        question = questionRepository.save(new Question("title", "contents"));
        answer = answerRepository.save(new Answer(user, question, "contents"));
    }

    @Test
    @DisplayName("save 검증 테스트")
    void saveTest() {
        assertAll(
            () -> assertThat(answer.getId()).isNotNull(),
            () -> assertThat(answer.isOwner(user)).isTrue(),
            () -> assertThat(answer.getQuestion()).isEqualTo(question),
            () -> assertThat(answer.getContents()).isEqualTo("contents"),
            () -> assertThat(answer.isDeleted()).isFalse()
    }

    @Test
    @DisplayName("findByQuestionIdAndDeletedFalse 검증 테스트")
    void findByQuestionIdAndDeletedFalseTest() {
        List<Answer> result = answerRepository.findByQuestionIdAndDeletedFalse(
            answer.getQuestion().getId());

        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result).contains(answer),
            () -> assertThat(result.get(0)).isEqualTo(answer)
        );
    }

    @Test
    @DisplayName("findByIdAndDeletedFalse 검증 테스트")
    void findByIdAndDeletedFalseTest() {
        Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(answer.getId());
        assertAll(
            () -> assertThat(actual.isPresent()).isTrue(),
            () -> assertThat(actual.get()).isEqualTo(answer)
        );
    }

}
