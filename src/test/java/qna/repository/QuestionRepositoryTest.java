package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;
import qna.domain.QuestionTest;
import qna.domain.UserTest;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void save() {
        // given & when
        final Question actual = questionRepository.save(QuestionTest.Q1);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getTitle()).isEqualTo(QuestionTest.Q1.getTitle()),
                () -> assertThat(actual.getContents()).isEqualTo(QuestionTest.Q1.getContents()),
                () -> assertThat(actual.getWriterId()).isEqualTo(QuestionTest.Q1.getWriterId())
        );
    }

    @Test
    void update() {
        // given
        final Question question = questionRepository.save(QuestionTest.Q1);
        final long expected = question.getWriterId();

        // when
        question.setWriterId(UserTest.SANJIGI.getId());
        final Question actual = questionRepository.findById(question.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(actual.getWriterId()).isNotEqualTo(expected);

    }

    @Test
    void delete() {
        // given
        final Question question = questionRepository.save(QuestionTest.Q1);

        // when
        questionRepository.delete(question);
        final Optional<Question> actual = questionRepository.findById(question.getId());

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void findById() {
        // given
        final Question expected = questionRepository.save(QuestionTest.Q1);

        // when
        final Question actual = questionRepository.findById(expected.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findByDeletedFalse() {
        // given
        final Question question = questionRepository.save(QuestionTest.Q1);
        final Question question2 = questionRepository.save(QuestionTest.Q2);

        // when
        final List<Question> actual = questionRepository.findByDeletedFalse();

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    void findByIdAndDeletedFalse() {
        // given
        final Question question = questionRepository.save(QuestionTest.Q1);

        // when
        final Question actual = questionRepository.findByIdAndDeletedFalse(question.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(question).isEqualTo(actual);
    }
}
