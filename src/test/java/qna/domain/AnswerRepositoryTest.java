package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @AfterEach
    void afterEach() {
        answerRepository.deleteAll();
    }

    @Test
    @DisplayName("생성된 Answer의 contents는 실제 저장한 contents와 동일해야 한다.")
    void saveTest() {
        // given
        Answer savedAnswer = answerRepository.save(AnswerTest.A1);

        // when
        Answer actualAnswer = AnswerTest.A1;

        // then
        assertAll(
            () -> assertThat(savedAnswer).isNotNull(),
            () -> assertThat(savedAnswer.getContents()).isEqualTo(actualAnswer.getContents())
        );
    }

    @Test
    @DisplayName("생성된 Answer와 그 id로 조회한 Answer는 동일성이 보장되어야 한다.")
    void findByIdTest() {
        // given
        Answer savedAnswer = answerRepository.save(AnswerTest.A1);

        // when
        Answer foundAnswer = answerRepository.findById(savedAnswer.getId()).orElse(null);

        // then
        assertAll(
            () -> assertThat(foundAnswer).isEqualTo(savedAnswer),
            () -> assertThat(foundAnswer.getId()).isEqualTo(savedAnswer.getId())
        );
    }

    @Test
    @DisplayName("not null 컬럼들은 반드시 값이 존재해야 한다.")
    void notNullColumnsTest() {
        // given
        Answer savedAnswer = answerRepository.save(AnswerTest.A1);

        // when
        Answer foundAnswer = answerRepository.findById(savedAnswer.getId()).orElse(null);

        // then
        assertAll(
            () -> assertThat(foundAnswer.getCreatedAt()).isBefore(LocalDateTime.now()),
            () -> assertThat(foundAnswer.isDeleted()).isFalse()
        );
    }

    @Test
    @DisplayName("Answer id로 Answer 조회하면 해당되는 Answer가 조회되어야 한다.")
    void findByIdAndDeletedFalseTest() {
        // given
        Answer savedAnswer = answerRepository.save(AnswerTest.A1);

        // when
        Optional<Answer> foundAnswer = answerRepository.findByIdAndDeletedFalse(
            savedAnswer.getId());

        // then
        Assertions.assertAll(
            () -> assertThat(foundAnswer.isPresent()).isTrue(),
            () -> assertThat(foundAnswer.orElse(null)).isSameAs(savedAnswer)
        );
    }

    @Test
    @DisplayName("Question id로 Answer List를 조회하면 List 목록이 일치해야 한다.")
    void findByQuestionIdAndDeletedFalseTest() {
        // given
        Answer savedAnswer1 = answerRepository.save(AnswerTest.A1);
        Answer savedAnswer2 = answerRepository.save(AnswerTest.A2);

        // when
        List<Answer> foundAnswers = answerRepository.findByQuestionIdAndDeletedFalse(
            QuestionTest.Q1.getId());

        // then
        assertThat(foundAnswers).containsExactly(savedAnswer1, savedAnswer2);
    }

    @Test
    @DisplayName("Update된 Contents가 일치해야 한다.")
    void updateAnswerTest() {
        // given
        Answer savedAnswer = answerRepository.save(AnswerTest.A1);
        savedAnswer.setContents("Answers Contents1 - Updated");

        // when
        Optional<Answer> foundAnswer = answerRepository.findByIdAndDeletedFalse(
            savedAnswer.getId());

        // then
        Assertions.assertAll(
            () -> assertThat(foundAnswer.isPresent()).isTrue(),
            () -> assertThat(foundAnswer.orElse(null)).isSameAs(savedAnswer)
        );
    }

    @Test
    @DisplayName("삭제된 Answer 조회시 존재하지 않고, 삭제되지 않은 Answer 조회시 정상조회 되어야 한다.")
    void deleteAnswerTest() {
        // given
        Answer savedAnswer1 = answerRepository.save(AnswerTest.A1);
        Answer savedAnswer2 = answerRepository.save(AnswerTest.A2);
        answerRepository.delete(savedAnswer1);
        answerRepository.flush();

        // when
        Optional<Answer> foundAnswer1 = answerRepository.findById(savedAnswer1.getId());
        Optional<Answer> foundAnswer2 = answerRepository.findById(savedAnswer2.getId());

        // then
        Assertions.assertAll(
            () -> assertThat(foundAnswer1).isNotPresent(),
            () -> assertThat(foundAnswer2).isPresent()
        );
    }

    @Test
    @DisplayName("삭제된 Answer List 조회시 비어있어야 한다.")
    public void deletAnswerListTest() {
        // given
        Answer savedAnswer1 = answerRepository.save(AnswerTest.A1);
        Answer savedAnswer2 = answerRepository.save(AnswerTest.A2);
        answerRepository.delete(savedAnswer1);
        answerRepository.delete(savedAnswer2);
        answerRepository.flush();

        // when
        List<Answer> foundAnswers = answerRepository.findByQuestionIdAndDeletedFalse(
            QuestionTest.Q1.getId());

        // then
        assertThat(foundAnswers).isEmpty();
    }
}
