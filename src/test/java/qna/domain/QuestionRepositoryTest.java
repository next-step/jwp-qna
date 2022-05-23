package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;


@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);
    }

    @DisplayName("삭제되지 않은 Question 조회")
    @Test
    void findByDeletedFalse() {
        //given
        QuestionTest.Q1.setDeleted(true);
        questionRepository.save(QuestionTest.Q1);
        final Question expected = questionRepository.save(QuestionTest.Q2);

        //when
        final List<Question> actual = questionRepository.findByDeletedFalse();

        //then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual).contains(expected)
        );
    }

    @DisplayName("id로 삭제되지 않은 Question 조회")
    @Test
    void findByIdAndDeletedFalse() {
        //given
        final Question expected = questionRepository.save(QuestionTest.Q1);

        //when
        final Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(expected.getId());

        //then
        assertAll(
                () -> assertThat(actual.isPresent()).isTrue(),
                () -> assertThat(actual.get()).isEqualTo(expected)
        );
    }

    @DisplayName("title의 길이는 지정된 length까지만 허용된다.")
    @Test
    void save_invalid_length() {
        Question question = new Question(
                "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz",
                "content"
        );
        assertThatThrownBy(() -> questionRepository.save(question))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("question 저장")
    @Test
    void save() {
        //given
        final Question expected = questionRepository.save(QuestionTest.Q1);

        //when
        final Optional<Question> actual = questionRepository.findById(expected.getId());

        //then
        assertAll(
                () -> assertThat(actual.isPresent()).isTrue(),
                () -> assertThat(actual.get()).isSameAs(expected)
        );
    }
}
