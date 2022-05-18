package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void Answer_저장() {
        Answer answer = AnswerTest.A1;
        Answer result = answerRepository.save(AnswerTest.A1);

        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getContents()).isEqualTo(answer.getContents())
        );
    }

    @Test
    void Answer_단건_조회() {
        Answer answer = answerRepository.save(AnswerTest.A1);

        assertThat(answerRepository.findById(answer.getId()).get())
                .usingRecursiveComparison()
                .isEqualTo(answer);
    }

    @Test
    void Answer_전체_조회() {
        answerRepository.save(AnswerTest.A1);
        answerRepository.save(AnswerTest.A2);

        assertThat(answerRepository.findAll()).hasSize(2);
    }

    @Test
    void Answer_삭제여부_컬럼이_false인_단건_조회() {
        Answer answer = answerRepository.save(AnswerTest.A1);

        assertThat(answerRepository.findByIdAndDeletedFalse(answer.getId()).get()).isEqualTo(answer);

        Answer findAnswer = answerRepository.findById(answer.getId()).get();
        findAnswer.setDeleted(true);

        assertThat(answerRepository.findByIdAndDeletedFalse(answer.getId()).isPresent()).isFalse();
    }

    @Test
    void Answer_findByQuestionId_삭제여부_컬럼이_false인_전체_조회() {
        Answer answer = answerRepository.save(AnswerTest.A1);

        assertThat(answerRepository.findByQuestionIdAndDeletedFalse(AnswerTest.A1.getQuestionId())).hasSize(1);

        Answer findAnswer = answerRepository.findById(answer.getId()).get();
        findAnswer.setDeleted(true);

        assertThat(answerRepository.findByQuestionIdAndDeletedFalse(AnswerTest.A1.getQuestionId()).size()).isZero();
    }

}