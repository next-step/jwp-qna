package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("답변 저장소 테스트")
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @BeforeEach
    void 테스트_수행_전_데이터_일괄삭제() {
        answerRepository.deleteAll();
    }

    @Test
    @DisplayName("답변 저장")
    void 저장() {
        Answer answer = AnswerTest.A1;
        Answer savedAnswer = answerRepository.save(answer);

        assertThat(savedAnswer.getId()).isNotNull();
        assertThat(answer.getQuestionId()).isEqualTo(savedAnswer.getQuestionId());
        assertThat(answer.getContents()).isEqualTo(savedAnswer.getContents());
        assertThat(answer.getWriterId()).isEqualTo(savedAnswer.getWriterId());
        assertThat(answer.isDeleted()).isEqualTo(savedAnswer.isDeleted());
    }

    @Test
    @DisplayName("답변 수정")
    void 수정() {
        Answer answer = answerRepository.save(AnswerTest.A1);
        answer.updateContents("답변 수정 테스트");
        Answer updatedAnswer = answerRepository.findById(answer.getId()).get();

        assertThat(updatedAnswer.getContents()).isEqualTo(answer.getContents());
    }

    @Test
    @DisplayName("답변 삭제")
    void 삭제() {
        Answer answer = answerRepository.save(AnswerTest.A1);
        answerRepository.delete(answer);

        assertThat(answerRepository.findById(answer.getId())).isEmpty();
    }

    @Test
    @DisplayName("답변 ID로 삭제되지 않은 답변 조회")
    void 답변_ID로_삭제되지_않은_답변_조회() {
        Answer savedAnswer = answerRepository.save(AnswerTest.A1);
        Answer expected = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId()).get();

        assertThat(savedAnswer.getId()).isEqualTo(expected.getId());
        assertThat(savedAnswer.getQuestionId()).isEqualTo(expected.getQuestionId());
        assertThat(savedAnswer.getContents()).isEqualTo(expected.getContents());
        assertThat(savedAnswer.getWriterId()).isEqualTo(expected.getWriterId());
        assertThat(savedAnswer.isDeleted()).isEqualTo(expected.isDeleted());
    }

    @Test
    @DisplayName("질문 ID로 삭제되지 않은 답변 목록 조회")
    void 질문_ID로_삭제되지_않은_답변목록_조회() {
        answerRepository.save(AnswerTest.A1);
        answerRepository.save(AnswerTest.A2);
        List<Answer> answerList = answerRepository.findByQuestionIdAndDeletedFalse(AnswerTest.A1.getQuestionId());

        assertThat(answerList).hasSize(2);
        assertThat(answerList).containsAll(Arrays.asList(AnswerTest.A1, AnswerTest.A2));
    }
}
