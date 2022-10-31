package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answerRepository;

    @BeforeEach
    void setUp() {
        answerRepository.deleteAllInBatch();
    }

    @Test
    void 답변_저장_테스트() {
        Answer answer = answerRepository.save(A1);
        assertAll(
                () -> assertThat(answer.getId()).isNotNull(),
                () -> assertThat(answer.getContents()).isEqualTo(A1.getContents()),
                () -> assertThat(answer.isDeleted()).isFalse()
        );
    }

    @Test
    void 답변_저장_후_삭제되지_않은_답변_ID로_조회_테스트() {
        Answer saveAnswer = answerRepository.save(A1);
        Long saveAnswerId = saveAnswer.getId();
        answerRepository.save(A2);
        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(saveAnswerId);

        assertThat(findAnswer.isPresent()).isTrue();
        findAnswer.ifPresent(answer -> assertAll(
                () -> assertThat(answer).isEqualTo(saveAnswer),
                () -> assertThat(answer.isDeleted()).isFalse(),
                () -> assertThat(answer).isNotEqualTo(A2)
        ));
    }

    @Test
    void 답변_저장_후_삭제여부_true로_업데이트하여_조회_안됨_테스트() {
        Answer saveAnswer = answerRepository.save(A1);
        Long saveAnswerId = saveAnswer.getId();

        saveAnswer.setDeleted(true);
        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(saveAnswerId);

        assertThat(findAnswer.isPresent()).isFalse();
    }

    @Test
    void 답변_조회_후_삭제하면_조회가_되지_않음_테스트() {
        Answer saveAnswer = answerRepository.save(A2);
        Long saveAnswerId = saveAnswer.getId();
        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(saveAnswerId);

        assertThat(findAnswer.isPresent()).isTrue();
        findAnswer.ifPresent(answer -> answerRepository.delete(answer));
        Optional<Answer> deletedAnswer = answerRepository.findByIdAndDeletedFalse(saveAnswerId);

        assertThat(deletedAnswer.isPresent()).isFalse();
    }
}
