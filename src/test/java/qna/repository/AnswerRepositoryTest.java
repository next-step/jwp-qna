package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.QuestionTest;
import qna.domain.UserTest;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    private Answer answer;

    @BeforeEach
    void setUp() {
        answer = new Answer(3L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents3");
    }

    @Test
    void save() {
        // given
        final Answer expected = new Answer(4L, UserTest.SANJIGI, QuestionTest.Q2, "Answers Contents4");

        // when
        final Answer actual = answerRepository.save(expected);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriterId()).isEqualTo(expected.getWriterId()),
                () -> assertThat(actual.getQuestionId()).isEqualTo(expected.getQuestionId()),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    void findById() {
        // given
        final Answer expected = answerRepository.save(answer);

        // when
        final Answer actual = answerRepository.findById(expected.getId()).orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAll() {
        // given
        final Answer expected = answerRepository.save(answer);

        // when
        final List<Answer> actual = answerRepository.findAll();

        // then
        assertThat(actual).hasSize(1);
    }

    @Test
    void update() {
        // given
        final Answer expected = answerRepository.save(answer);
        final String contents = expected.getContents();

        // when
        expected.setContents(contents + " new");
        final Answer actual = answerRepository.findById(expected.getId()).orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(actual.getContents()).isNotEqualTo(contents);
    }

    @Test
    void delete() {
        // given
        final Answer expected = answerRepository.save(answer);

        // when
        answerRepository.deleteById(expected.getId());
        final Optional<Answer> actual = answerRepository.findById(expected.getId());

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void findByQuestionIdAndDeletedFalse() {
        // given
        final Answer expected = answerRepository.save(answer);

        // when
        final List<Answer> actual = answerRepository.findByQuestionIdAndDeletedFalse(expected.getQuestionId());

        // then
        assertThat(actual).hasSize(1);
    }

    @Test
    void findByQuestionIdAndDeletedFalseSetDeletedTrue() {
        // given
        final Answer expected = answerRepository.save(answer);

        // when
        expected.setDeleted(true);
        final List<Answer> actual = answerRepository.findByQuestionIdAndDeletedFalse(expected.getQuestionId());

        // then
        assertThat(actual).hasSize(0);
    }

    @Test
    void findByIdAndDeletedFalse() {
        // given
        final Answer expected = answerRepository.save(answer);

        // when
        final Answer actual = answerRepository.findByIdAndDeletedFalse(expected.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findByIdAndDeletedFalseSetDeletedTrue() {
        // given
        final Answer expected = answerRepository.save(answer);

        // when
        expected.setDeleted(true);
        final Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(expected.getId());

        // then
        assertThat(actual).isEmpty();
    }
}
