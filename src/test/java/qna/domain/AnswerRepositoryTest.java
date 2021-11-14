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

    private User writer;
    private Question question;
    private LocalDateTime startTime;

    @BeforeEach
    void setUp() {
        startTime = LocalDateTime.now();

        writer = new User();
        writer.setId(1L);

        question = new Question();
        question.setId(1L);
    }

    @DisplayName("답변 생성")
    @Test
    void save() {
        Answer expected = new Answer(writer, question, "contents");

        Answer actual = answerRepository.save(expected);

        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
            () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
            () -> assertThat(actual.getWriterId()).isEqualTo(expected.getWriterId()),
            () -> assertThat(actual.getQuestionId()).isEqualTo(expected.getQuestionId()),
            () -> assertThat(actual.getCreatedAt()).isAfterOrEqualTo(startTime),
            () -> assertThat(actual.getUpdatedAt()).isAfterOrEqualTo(startTime)
        );
    }

    @DisplayName("삭제되지 않은 답변 질문 id로 조회")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        Answer expected1 = new Answer(writer, question, "contents");
        Answer expected2 = new Answer(writer, question, "contents2");
        Answer deletedAnswer = new Answer(writer, question, "contents3");
        deletedAnswer.setDeleted(true);
        answerRepository.save(expected1);
        answerRepository.save(expected2);
        answerRepository.save(deletedAnswer);

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(1L);

        assertIterableEquals(answers, Arrays.asList(expected1, expected2));
    }

    @DisplayName("삭제되지 않은 답변 id로 조회")
    @Test
    void findByIdAndDeletedFalse() {
        Answer expected = new Answer(writer, question, "contents");
        Answer deletedAnswer = new Answer(writer, question, "contents3");
        deletedAnswer.setDeleted(true);
        answerRepository.save(expected);
        answerRepository.save(deletedAnswer);

        Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(1L);
        Optional<Answer> actualNull = answerRepository.findByIdAndDeletedFalse(2L);

        assertAll(
            () -> assertThat(actual.isPresent()),
            () -> assertThat(actual.get()).isEqualTo(expected),
            () -> assertThat(actualNull.isPresent()).isFalse()
        );
    }
}