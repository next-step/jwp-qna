package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("답변 저장 및 값 비교 테스트")
    void save() {
        final Answer expected = AnswerTest.A1;
        final Answer actual = answerRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriterId()).isEqualTo(expected.getWriterId()),
                () -> assertThat(actual.getQuestionId()).isEqualTo(expected.getQuestionId()),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    @DisplayName("질문 작성자 id로 삭제되지 않은 답변 목록 조회")
    void findByQuestionIdAndDeletedFalse() {
        final Answer answer1 = answerRepository.save(AnswerTest.A1);
        final Answer answer2 = answerRepository.save(AnswerTest.A2);
        answer1.setDeleted(true);

        List<Answer> founds = answerRepository.findByQuestionIdAndDeletedFalse(answer1.getQuestionId());

        assertAll(
                () -> assertThat(founds).hasSize(1),
                () -> assertThat(founds).doesNotContain(answer1),
                () -> assertThat(founds).containsExactly(answer2)
        );
    }

    @Test
    @DisplayName("id로 삭제되지 않은 질문 목록 조회")
    void findByIdAndDeletedFalse() {
        final Answer answer1 = answerRepository.save(AnswerTest.A1);
        final Answer answer2 = answerRepository.save(AnswerTest.A2);
        answer1.setDeleted(true);

        Optional<Answer> foundsAnswer1 = answerRepository.findByIdAndDeletedFalse(answer1.getId());
        Optional<Answer> foundsAnswer2 = answerRepository.findByIdAndDeletedFalse(answer2.getId());

        assertAll(
                () -> assertThat(foundsAnswer1.isPresent()).isFalse(),
                () -> assertThat(foundsAnswer2.isPresent()).isTrue()
        );
    }
}
