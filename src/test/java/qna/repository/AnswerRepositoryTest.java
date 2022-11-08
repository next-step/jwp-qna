package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.domain.AnswerTest;

@DisplayName("Answer_관련_테스트")
@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @DisplayName("저장_확인")
    @Test
    void save() {
        Answer answer = answerRepository.save(AnswerTest.A1);
        assertAll(
                () -> assertThat(answer.getId()).isNotNull(),
                () -> assertThat(answer.getContents()).isEqualTo(AnswerTest.A1.getContents()),
                () -> assertThat(answer.getWriterId()).isEqualTo(AnswerTest.A1.getWriterId()),
                () -> assertThat(answer.getQuestionId()).isEqualTo(AnswerTest.A1.getQuestionId())
        );
    }

    @DisplayName("findByQuestionIdAndDeletedFalse_question_id_기준으로_조회")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        Answer answer1 = answerRepository.save(AnswerTest.A1);
        Answer answer2 = answerRepository.save(AnswerTest.A2);
        List<Answer> expectedResults = answerRepository.findByQuestionIdAndDeletedFalse(answer1.getQuestionId());
        assertThat(expectedResults).contains(answer1);
    }

    @DisplayName("findByIdAndDeletedFalse_deleted_false인_data_조회")
    @Test
    void findByIdAndDeletedFalse() {
        Answer answer1 = answerRepository.save(AnswerTest.A1);
        Answer expectedResult = answerRepository.findByIdAndDeletedFalse(answer1.getId()).orElse(null);
        assertThat(expectedResult).isEqualTo(answer1);
    }
}
