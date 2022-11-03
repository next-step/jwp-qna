package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.AnswerTest;
import qna.domain.QuestionTest;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @BeforeEach
    void setUp() {
        answerRepository.deleteAll();
    }

    @DisplayName("답변을 저장 후 확인")
    @Test
    void save() {
        Answer answer = answerRepository.save(AnswerTest.A1);

        assertAll(
            () -> assertThat(answer.getId()).isNotNull(),
            () -> assertThat(answer.getWriterId()).isEqualTo(AnswerTest.A1.getWriterId()),
            () -> assertThat(answer.getContents()).isEqualTo(AnswerTest.A1.getContents()),
            () -> assertThat(answer.getQuestionId()).isEqualTo(AnswerTest.A1.getQuestionId())
        );
    }

    @DisplayName("답변을 저장 후 조회 확인")
    @Test
    void findAll() {
        Answer answer1 = answerRepository.save(AnswerTest.A1);
        Answer answer2 = answerRepository.save(AnswerTest.A2);

        List<Answer> result = answerRepository.findAll();

        assertAll(
            () -> assertThat(result).hasSize(2),
            () -> assertThat(result).contains(answer1, answer2)
        );
    }

    @DisplayName("답변을 저장 후 수정 확인")
    @Test
    void update() {
        Answer answer = answerRepository.save(AnswerTest.A1);
        answer.toQuestion(QuestionTest.Q1);

        Optional<Answer> result = answerRepository.findById(answer.getId());

        assertAll(
            () -> assertThat(result).isPresent(),
            () -> assertThat(result.get().getQuestionId()).isEqualTo(answer.getQuestionId())
        );
    }

    @DisplayName("답변을 저장 후 삭제 확인")
    @Test
    void remove() {
        Answer answer = answerRepository.save(AnswerTest.A1);
        answerRepository.delete(answer);

        Optional<Answer> result = answerRepository.findById(answer.getId());

        assertThat(result).isNotPresent();
    }

    @DisplayName("질문 식별자로 삭제되지 않는 답변을 조회")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        Answer answer = answerRepository.save(AnswerTest.A1);

        List<Answer> result = answerRepository.findByQuestionIdAndDeletedFalse(
            answer.getQuestionId());

        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result).contains(answer),
            () -> assertThat(result.get(0)).isEqualTo(answer)
        );
    }

    @DisplayName("답변 식별자로 삭제되지 않는 답변을 조회")
    @Test
    void findByIdAndDeletedFalse() {
        Answer answer = answerRepository.save(AnswerTest.A1);

        Optional<Answer> result = answerRepository.findByIdAndDeletedFalse(answer.getId());

        assertAll(
            () -> assertThat(result.isPresent()).isTrue(),
            () -> assertThat(result.get()).isEqualTo(answer)
        );
    }
}
