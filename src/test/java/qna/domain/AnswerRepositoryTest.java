package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answerRepository;

    @DisplayName("Answer을 저장할 수 있다.")
    @Test
    void save() {
        Answer actual = answerRepository.save(AnswerTest.A1);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriterId()).isEqualTo(AnswerTest.A1.getWriterId()),
                () -> assertThat(actual.getQuestionId()).isEqualTo(AnswerTest.A1.getQuestionId()),
                () -> assertThat(actual.getContents()).isEqualTo(AnswerTest.A1.getContents())
        );
    }

    @DisplayName("저장된 Answer을 조회할 수 있다.")
    @Test
    void find() {
        Answer actual = answerRepository.save(AnswerTest.A1);

        Optional<Answer> findAnswer = answerRepository.findById(actual.getId());

        assertThat(findAnswer).isPresent();
    }

    @DisplayName("저장된 Answer을 삭제할 수 있다.")
    @Test
    void delete() {
        Answer actual = answerRepository.save(AnswerTest.A1);

        answerRepository.delete(actual);
        Optional<Answer> findAnswer = answerRepository.findById(actual.getId());

        assertThat(findAnswer).isNotPresent();
    }

    @DisplayName("저장된 Answer의 값을 변경할 수 있다.")
    @Test
    void update() {
        Answer actual = answerRepository.save(new Answer(UserTest.SANJIGI, QuestionTest.Q1, ""));

        actual.toQuestion(QuestionTest.Q2);
        Optional<Answer> findAnswer = answerRepository.findById(actual.getId());

        assertThat(findAnswer.orElseThrow(NotFoundException::new).getQuestionId()).isEqualTo(QuestionTest.Q2.getId());

    }

    @DisplayName("questionId로 저장된 Answer 중 삭제되지 않은 것을 조회할 수 있다.")
    @Test
    void find_by_question_id_and_deleted_false() {
        Answer answerA1 = answerRepository.save(new Answer(UserTest.SANJIGI, QuestionTest.Q1, ""));
        Answer answerA2 = answerRepository.save(new Answer(UserTest.SANJIGI, QuestionTest.Q1, ""));

        answerA1.setDeleted(true);
        List<Answer> expect = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());

        assertThat(expect).containsExactly(answerA2);
    }

    @DisplayName("id로 저장된 Answer 중 삭제되지 않은 것을 조회할 수 있다.")
    @Test
    void find_by_id_and_deleted_false() {
        Answer actual1 = answerRepository.save(new Answer(UserTest.SANJIGI, QuestionTest.Q1, ""));
        Answer actual2 = answerRepository.save(new Answer(UserTest.SANJIGI, QuestionTest.Q1, ""));

        actual2.setDeleted(true);
        Optional<Answer> expect1 = answerRepository.findByIdAndDeletedFalse(actual1.getId());
        Optional<Answer> expect2 = answerRepository.findByIdAndDeletedFalse(actual2.getId());

        assertAll(
                () -> assertThat(expect1).isPresent(),
                () -> assertThat(expect2).isNotPresent()
        );

    }
}
