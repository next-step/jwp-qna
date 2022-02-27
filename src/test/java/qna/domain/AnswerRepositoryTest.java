package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest //JPA(DB) 와 관련된 Test
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void save() {
        final Answer actual = answerRepository.save(AnswerTest.A1);
        assertThat(actual.getId()).isEqualTo(AnswerTest.A1.getId());
    }

    @Test
    void findById() {
        final Answer answer = answerRepository.save(AnswerTest.A2);
        final Optional<Answer> actual = answerRepository.findById(AnswerTest.A2.getId());
        assertThat(actual.get()).isEqualTo(answer);
    }

    @Test
    @DisplayName("삭제되지 않은 Answer 리스트를 QuestionId 로 찾는다")
    void findByQuestionIdAndDeletedFalse() {
        answerRepository.saveAll(Arrays.asList(AnswerTest.A3, AnswerTest.A4));
        final List<Answer> findAnswers = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());
        for (Answer answer : findAnswers) {
            assertThat(answer.isDeleted()).isFalse();
        }
    }

    @Test
    @DisplayName("삭제되지 않은 Answer 를 id 로 찾는다")
    void findByIdAndDeletedFalse() {
        answerRepository.saveAll(Arrays.asList(AnswerTest.A3, AnswerTest.A4));
        final Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(AnswerTest.A3.getId());
        assertThat(findAnswer.get().getId()).isEqualTo(AnswerTest.A3.getId());
        assertThat(findAnswer.get().isDeleted()).isFalse();
    }
}