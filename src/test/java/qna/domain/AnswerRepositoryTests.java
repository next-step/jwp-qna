package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DisplayName("답변 저장소 테스트")
class AnswerRepositoryTests {
    @Autowired
    private AnswerRepository answerRepository;

    @BeforeEach
    void before() {
        answerRepository.deleteAll();
    }

    @Test
    @DisplayName("답변을 저장한다.")
    void save() {
        Answer expected = AnswerTest.A1;
        Answer answer = answerRepository.save(expected);

        assertAll(
                () -> assertThat(answer.getId()).isNotNull(),
                () -> assertThat(answer.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(answer.getQuestionId()).isEqualTo(expected.getQuestionId()),
                () -> assertThat(answer.getWriterId()).isEqualTo(expected.getWriterId()),
                () -> assertThat(answer.isDeleted()).isEqualTo(expected.isDeleted())
        );
    }
    @Test
    @DisplayName("식별자로 답변을 조회한다.")
    void findById() {
        Answer expected = answerRepository.save(AnswerTest.A1);

        Answer answer = answerRepository.findById(expected.getId()).get();

        assertAll(
                () -> assertThat(answer.getId()).isEqualTo(expected.getId()),
                () -> assertThat(answer.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(answer.getQuestionId()).isEqualTo(expected.getQuestionId()),
                () -> assertThat(answer.getWriterId()).isEqualTo(expected.getWriterId()),
                () -> assertThat(answer.isDeleted()).isEqualTo(expected.isDeleted())
        );
    }
    @Test
    @DisplayName("질문 식별자로 삭제되지 않은 답변을 조회한다.")
    void findByQuestionIdAndDeletedFalse() {
        Answer expected = answerRepository.save(AnswerTest.A1);

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(expected.getQuestionId());
        assertThat(answers).hasSize(1);
    }

    @Test
    @DisplayName("식별자로 삭제되지 않은 답변을 조회한다.")
    void findByIdAndDeletedFalse() {
        Answer expected = answerRepository.save(AnswerTest.A1);

        Answer answer = answerRepository.findByIdAndDeletedFalse(expected.getId()).get();
        assertAll(
                () -> assertThat(answer.getId()).isEqualTo(expected.getId()),
                () -> assertThat(answer.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(answer.getQuestionId()).isEqualTo(expected.getQuestionId()),
                () -> assertThat(answer.getWriterId()).isEqualTo(expected.getWriterId()),
                () -> assertThat(answer.isDeleted()).isEqualTo(expected.isDeleted())
        );
    }

    @Test
    @DisplayName("답변을 삭제한다.")
    void delete() {
        Answer expected = answerRepository.save(AnswerTest.A1);

        answerRepository.delete(expected);

        assertThat(answerRepository.findById(expected.getId())).isEmpty();
    }
}
