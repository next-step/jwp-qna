package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;
import qna.domain.UserTest;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    private Question question;

    @BeforeEach
    void setUp() {
        question = new Question(3L, "title3", "contents3").writeBy(UserTest.JAVAJIGI);
    }

    @Test
    void save() {
        // given
        final Question expected = new Question(4L, "title4", "contents4").writeBy(UserTest.JAVAJIGI);

        // when
        final Question actual = questionRepository.save(expected);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle()),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(actual.getWriterId()).isEqualTo(expected.getWriterId())
        );
    }

    @Test
    void update() {
        // given
        final Question expected = questionRepository.save(question);
        final String contents = expected.getContents();

        // when
        expected.setContents(contents + "2");
        final Question actual = questionRepository.findById(expected.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(actual.getContents()).isNotEqualTo(contents);
    }

    @Test
    void delete() {
        // given
        final Question expected = questionRepository.save(question);

        // when
        questionRepository.delete(expected);
        final Optional<Question> actual = questionRepository.findById(expected.getId());

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void findById() {
        // given
        final Question expected = questionRepository.save(question);

        // when
        final Question actual = questionRepository.findById(expected.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findByDeletedFalseSetDeleted() {
        // given
        final Question expected = questionRepository.save(question);

        // when
        expected.setDeleted(true);
        final List<Question> actual = questionRepository.findByDeletedFalse();

        // then
        assertThat(actual).hasSize(0);
    }

    @Test
    void findByDeletedFalse() {
        // given
        final Question expected = questionRepository.save(question);

        // when
        final List<Question> actual = questionRepository.findByDeletedFalse();

        // then
        assertThat(actual).hasSize(1);
    }

    @Test
    void findByIdAndDeletedFalseSetDeleted() {
        // given
        final Question expected = questionRepository.save(question);

        // when
        expected.setDeleted(true);
        final Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(expected.getId());

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void findByIdAndDeletedFalse() {
        // given
        final Question expected = questionRepository.save(question);

        // when
        final Question actual = questionRepository.findByIdAndDeletedFalse(expected.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(expected).isEqualTo(actual);
    }
}
