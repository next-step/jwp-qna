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
        List<Answer> actualAnswers = answerRepository.saveAll(Arrays.asList(AnswerTest.A3, AnswerTest.A4));
        actualAnswers.get(0).setDeleted(true);

        final List<Answer> findAnswers = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());
        assertThat(findAnswers.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("삭제되지 않은 Answer 를 id 로 찾는다")
    void findByIdAndDeletedFalse() {
        List<Answer> actualAnswers = answerRepository.saveAll(Arrays.asList(AnswerTest.A3, AnswerTest.A4));
        actualAnswers.get(0).setDeleted(true);

        final Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(AnswerTest.A3.getId());
        assertThat(findAnswer.isPresent()).isFalse();
    }
}