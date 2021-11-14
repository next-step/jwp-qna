package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {

    private Answer answer;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        final User writer = userRepository.save(
            new User(1L, "javajigi", "password", "name", "javajigi@slipp.net")
        );
        final Question question = questionRepository.save(
            new Question("title1", "contents1").writeBy(writer)
        );
        answer = new Answer(writer, question, "Answers Contents1");
    }

    @Test
    void save() {
        final Answer actual = answerRepository.save(answer);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getWriterId()).isNotNull(),
            () -> assertThat(actual.getQuestionId()).isNotNull(),
            () -> assertThat(actual.getContents()).isNotNull()
        );
    }

    @Test
    void findByQuestionIdAndDeletedFalse() {
        answerRepository.save(answer);
        answer.setDeleted(false);
        final List<Answer> actual =
            answerRepository.findByQuestionIdAndDeletedFalse(answer.getQuestionId());
        assertThat(actual).hasSize(1);
    }

    @Test
    void findByIdAndDeletedFalse() {
        answerRepository.save(answer);
        answer.setDeleted(false);
        final Answer actual = answerRepository.findByIdAndDeletedFalse(answer.getId())
            .orElseThrow(NoSuchElementException::new);
        assertThat(actual.getId()).isEqualTo(answer.getId());
    }
}
