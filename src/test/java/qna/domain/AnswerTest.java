package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.QuestionTest.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @DisplayName("Entity 데이터를 DB에 저장하는 테스트")
    @Test
    void save() {
        // given
        final Answer actual = answerRepository.save(A1);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual).isEqualTo(A1)
        );
    }

    @DisplayName("DB에 저장할 때 반환된 Entity 와 DB 에서 조회한 데이터가 일치하는지 확인하는 테스트")
    @Test
    void findById() {
        // given
        final Answer expected = answerRepository.save(A1);

        // when
        final Optional<Answer> optAnswer = answerRepository.findById(A1.getId());
        final Answer actual = optAnswer.orElseThrow(IllegalArgumentException::new);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual).isEqualTo(expected),
            () -> assertThat(actual.isDeleted()).isEqualTo(expected.isDeleted())
        );
    }

    @DisplayName("DB에 저장된 데이터 수를 확인하는 테스트")
    @Test
    void count() {
        // given
        answerRepository.save(A1);
        answerRepository.save(A2);
        answerRepository.save(A1);

        // when
        final long actual = answerRepository.count();

        // when
        assertThat(actual).isEqualTo(2);
    }

    @DisplayName("deleted 값을 변경하면 조회 결과가 달라지는지 테스트")
    @Test
    void findByIdAndDeletedFalse() {
        // given
        final Answer savedAnswer = answerRepository.save(A1);
        final Answer savedAnswer2 = answerRepository.save(A2);
        savedAnswer2.setDeleted(true);

        // when
        final Optional<Answer> optionalAnswer = answerRepository.findByIdAndDeletedFalse(A1.getId());
        final Optional<Answer> optionalAnswer2 = answerRepository.findByIdAndDeletedFalse(A2.getId());

        // then
        final Answer actual = optionalAnswer.orElseThrow(IllegalArgumentException::new);
        assertThat(actual).isEqualTo(savedAnswer);
        assertThatThrownBy(() ->
            optionalAnswer2.orElseThrow(IllegalArgumentException::new)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Question ID와 deleted 값을 각각 설정해서 조회하는 테스트")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        // given
        final Answer savedAnswer = answerRepository.save(A1);
        savedAnswer.setQuestionId(Q1.getId());
        final Answer savedAnswer2 = answerRepository.save(A2);
        savedAnswer2.setQuestionId(Q1.getId());
        savedAnswer2.setDeleted(true);

        // when
        final List<Answer> actual = answerRepository.findByQuestionIdAndDeletedFalse(Q1.getId());

        // then
        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.size()).isEqualTo(1),
            () -> assertThat(actual.get(0)).isEqualTo(savedAnswer)
        );
    }

    @DisplayName("saveAll 과 findAll 테스트")
    @Test
    void findAll() {
        // given
        final List<Answer> answers = Arrays.asList(A1, A2);
        final List<Answer> savedAnswers = answerRepository.saveAll(answers);

        // when
        final List<Answer> findAll = answerRepository.findAll();

        // then
        assertAll(
            () -> assertThat(savedAnswers.size()).isEqualTo(2),
            () -> assertThat(findAll.size()).isEqualTo(2),
            () -> assertThat(savedAnswers).isEqualTo(findAll)
        );
    }

    @Test
    void delete() {
        // given
        answerRepository.save(A1);
        answerRepository.save(A2);
        answerRepository.delete(A1);
        answerRepository.deleteById(A2.getId());

        // when
        final long actual = answerRepository.count();

        // then
        assertThat(actual).isEqualTo(0);
    }
}
