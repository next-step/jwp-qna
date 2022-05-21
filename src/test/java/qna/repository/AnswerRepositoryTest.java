package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.AnswerTest;
import qna.domain.QuestionTest;
import qna.domain.UserTest;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void save() {
        // given & when
        final Answer actual = answerRepository.save(AnswerTest.A1);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriterId()).isEqualTo(AnswerTest.A1.getId()),
                () -> assertThat(actual.getQuestionId()).isEqualTo(AnswerTest.A1.getQuestionId()),
                () -> assertThat(actual.getContents()).isEqualTo(AnswerTest.A1.getContents())
        );
    }

    @Test
    void findById() {
        // given
        final Answer expected = answerRepository.save(AnswerTest.A1);

        // when
        final Answer actual = answerRepository.findById(expected.getId()).orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findAll() {
        // given
        final Answer answer1 = answerRepository.save(AnswerTest.A1);
        final Answer answer2 = answerRepository.save(AnswerTest.A2);

        final List<Answer> actual = answerRepository.findAll();

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    void update() {
        // given
        final Answer answer = answerRepository.save(AnswerTest.A1);
        final long expected = answer.getWriterId();

        // when
        answer.setWriterId(UserTest.SANJIGI.getId());
        final Answer actual = answerRepository.findById(answer.getId()).orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(expected).isNotEqualTo(actual.getWriterId());
    }

    @Test
    void delete() {
        // given
        final Answer answer = answerRepository.save(AnswerTest.A1);

        // when
        answerRepository.deleteById(answer.getId());
        final Optional<Answer> actual = answerRepository.findById(answer.getId());

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void findByQuestionIdAndDeletedFalse() {
        // given
        final Answer answer = answerRepository.save(AnswerTest.A1);
        final Answer answer2 = answerRepository.save(AnswerTest.A2);

        // when
        final List<Answer> actual = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    void findByIdAndDeletedFalse() {
        // given
        final Answer expected = answerRepository.save(AnswerTest.A1);

        // when
        final Answer actual = answerRepository.findByIdAndDeletedFalse(expected.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(expected).isEqualTo(actual);
    }
}
