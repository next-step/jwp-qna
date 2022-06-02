package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;

    @BeforeEach
    void deleteAll() {
        answerRepository.deleteAll();
    }

    @Test
    @DisplayName("답변을 등록할 수 있다.")
    void create() {
        answerRepository.save(AnswerTest.A1);

        Optional<Answer> findAnswer = answerRepository.findById(AnswerTest.A1.getId());

        assertThat(findAnswer.isPresent()).isTrue();
        assertThat(findAnswer.get().getId()).isEqualTo(AnswerTest.A1.getId());
    }

    @Test
    @DisplayName("질문에 등록된 답변을 찾을 수 있다.")
    void findByQuestionIdAndDeletedFalse() {
        answerRepository.save(AnswerTest.A1);
        answerRepository.save(AnswerTest.A2);

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(AnswerTest.A1.getQuestionId());

        assertThat(answers.size()).isEqualTo(2);
        assertThat(answers.get(0).getId()).isEqualTo(AnswerTest.A1.getId());
        assertThat(answers.get(1).getId()).isEqualTo(AnswerTest.A2.getId());
    }

    @Test
    @DisplayName("id로 삭제여부를 알 수 있다.")
    void findByIdAndDeletedFalse() {
        answerRepository.save(AnswerTest.A1);

        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(AnswerTest.A1.getId());

        assertThat(findAnswer.isPresent()).isTrue();
        assertThat(findAnswer.get().getId()).isEqualTo(AnswerTest.A1.getId());
        assertThat(findAnswer.get().isDeleted()).isFalse();
    }
}