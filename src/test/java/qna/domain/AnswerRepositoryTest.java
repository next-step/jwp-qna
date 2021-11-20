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
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private LocalDateTime startTime;
    private User user1;
    private Question question;
    private Answer answer1;
    private Answer answer2;

    @BeforeEach
    void setUp() {
        startTime = LocalDateTime.now();

        user1 = userRepository.save(new User("userId", "name", "password", "email"));
        User user2 = userRepository.save(new User("userId2", "name", "password", "email"));
        question = questionRepository.save(new Question("title", "contents").writeBy(user1));
        answer1 = answerRepository.save(new Answer(user1, question, "answer1"));
        answer2 = answerRepository.save(new Answer(user2, question, "answer2"));
    }

    @DisplayName("답변 생성")
    @Test
    void save() {
        Answer actual = answerRepository.findById(answer1.getId()).get();
        answerRepository.flush();

        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(answer1.getId()),
            () -> assertThat(actual.getContents()).isEqualTo(answer1.getContents()),
            () -> assertThat(actual.getWriter()).isEqualTo(answer1.getWriter()),
            () -> assertThat(actual.getQuestion()).isEqualTo(answer1.getQuestion()),
            () -> assertThat(actual.getCreatedAt()).isAfterOrEqualTo(startTime),
            () -> assertThat(actual.getUpdatedAt()).isAfterOrEqualTo(startTime)
        );
    }

    @DisplayName("삭제되지 않은 답변 질문 id로 조회")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        answer1.delete();
        answerRepository.save(answer1);

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());

        assertIterableEquals(answers, Arrays.asList(answer2));
    }

    @DisplayName("삭제되지 않은 답변 id로 조회")
    @Test
    void findByIdAndDeletedFalse() {
        answer2.delete();
        answerRepository.save(answer2);

        Answer deletedAnswer = answerRepository.save(new Answer(user1, question, "answer contents3"));
        deletedAnswer.delete();

        Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(answer1.getId());
        Optional<Answer> actualNull = answerRepository.findByIdAndDeletedFalse(deletedAnswer.getId());

        assertAll(
            () -> assertThat(actual.isPresent()),
            () -> assertThat(actual.get()).isEqualTo(answer1),
            () -> assertThat(actualNull.isPresent()).isFalse()
        );
    }
}