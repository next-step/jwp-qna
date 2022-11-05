package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @DisplayName("답변을 저장할 수 있다")
    @Test
    void save() {
        Answer result = answerRepository.save(A1);

        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.isDeleted()).isEqualTo(A1.isDeleted()),
                () -> assertThat(result.getContents()).isEqualTo(A1.getContents()),
                () -> assertThat(result.getQuestion()).isEqualTo(A1.getQuestion()),
                () -> assertThat(result.getWriter()).isEqualTo(A1.getWriter())
        );
    }

    @DisplayName("id로 조회할 수 있다")
    @Test
    void findById() {
        Answer expect = answerRepository.save(A1);
        Answer result = answerRepository.findByIdAndDeletedFalse(expect.getId()).get();
        assertThat(expect == result).isTrue();
    }

    @DisplayName("답변이 삭제되었을 경우, findByIdAndDeletedFalse 함수로 조회할 수 없다")
    @Test
    void findDeletedById() {
        Answer actual = answerRepository.save(A2);
        actual.setDeleted(true);

        Optional<Answer> result = answerRepository.findByIdAndDeletedFalse(actual.getId());

        assertThat(result).isNotPresent();
    }

    @DisplayName("질문의 id로 조회할 수 있다")
    @Test
    void findByQuestionId() {
        Answer actual = answerRepository.save(A1);

        Answer result = answerRepository.findByQuestionIdAndDeletedFalse(actual.getQuestion().getId()).get(0);

        assertThat(actual == result).isTrue();
    }

    @DisplayName("답변이 삭제되었을 경우, findByQuestionIdAndDeletedFalse 함수로 조회할 수 없다")
    @Test
    void findDeletedByQuestionId() {
        Answer actual = answerRepository.save(A2);
        actual.setDeleted(true);

        List<Answer> result = answerRepository.findByQuestionIdAndDeletedFalse(actual.getQuestion().getId());

        assertThat(result).hasSize(0);
    }
}
