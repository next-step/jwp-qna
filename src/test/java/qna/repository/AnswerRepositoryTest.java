package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.AnswerTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("answer save() 테스트를 진행한다")
    void saveAnswer() {
        Answer answer = AnswerTest.A1;
        Answer saveAnswer = answerRepository.save(answer);

        assertAll(
                () -> assertThat(saveAnswer.getId()).isNotNull(),
                () -> assertThat(saveAnswer.getWriterId()).isEqualTo(answer.getWriterId()),
                () -> assertThat(saveAnswer.getContents()).isEqualTo(answer.getContents()),
                () -> assertThat(saveAnswer.getQuestionId()).isEqualTo(answer.getQuestionId())
        );
    }

    @Test
    @DisplayName("answer을 저장하고 데이터에 존재하는지 찾아본다")
    void saveAnswerAndFind() {
        Answer saveAnswer = answerRepository.save(AnswerTest.A1);
        saveAnswer.setDeleted(false);
        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(saveAnswer.getId());

        assertThat(saveAnswer).isEqualTo(findAnswer.get());
    }

    @Test
    @DisplayName("answer가 삭제가 되는지 확인한다")
    void answerDelete() {
        Answer saveAnswer = answerRepository.save(AnswerTest.A1);

        answerRepository.delete(saveAnswer);

        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(saveAnswer.getId());

        assertThat(findAnswer).isEmpty();
        assertThat(findAnswer).isNotPresent();
    }

    @Test
    @DisplayName("삭제된 answer는 가져 올 수 없다.")
    void answerDeleteNotFind() {
        Answer saveAnswer = answerRepository.save(AnswerTest.A1);
        saveAnswer.setDeleted(true);

        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(saveAnswer.getId());

        assertThat(findAnswer).isEmpty();
        assertThat(findAnswer).isNotPresent();
    }

    @Test
    @DisplayName("QuestionId로 answer 리스트를 불러온다")
    void answerListByQuestionId() {
        Answer answerA = answerRepository.save(AnswerTest.A1);
        answerA.setDeleted(false);

        Answer answerB = answerRepository.save(AnswerTest.A2);
        answerB.setDeleted(false);

        List<Answer> result = answerRepository.findByQuestionIdAndDeletedFalse(answerB.getQuestionId());

        assertThat(result).hasSize(2);
        assertThat(result).contains(answerA, answerB);
    }

    @Test
    @DisplayName("QuestionId로 answer 리스트를 불러온다 (아무것도 없을경우)")
    void answerListByQuestionIdNotResult() {
        Answer answerA = answerRepository.save(AnswerTest.A1);
        answerA.setDeleted(true);

        Answer answerB = answerRepository.save(AnswerTest.A2);
        answerB.setDeleted(true);

        List<Answer> findAnswers = answerRepository.findByQuestionIdAndDeletedFalse(answerB.getQuestionId());

        assertThat(findAnswers).doesNotContain(answerA, answerB);
        assertThat(findAnswers).hasSize(0);
    }
}