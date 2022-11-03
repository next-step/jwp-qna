package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import qna.domain.Answer;
import qna.domain.AnswerTest;
import qna.domain.QuestionTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @BeforeEach
    void setUp() {
        answerRepository.deleteAll();
    }

    @Test
    @DisplayName("Answer 저장 테스트")
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
    @DisplayName("Answer 2건 저장 후 전체 조회 테스트")
    void saveAllAnswer() {
        Answer answer1 = AnswerTest.A1;
        Answer answer2 = AnswerTest.A2;
        answerRepository.saveAll(Arrays.asList(answer1, answer2));

        List<Answer> answers = answerRepository.findAll();

        assertThat(answers).hasSize(2);
    }

    @Test
    @DisplayName("Answer 저장 후 Answer 조회 테스트")
    void readAnswer() {
        Answer saveAnswer = answerRepository.save(AnswerTest.A1);
        Optional<Answer> findAnswer = answerRepository.findById(saveAnswer.getId());

        assertThat(findAnswer).isPresent();
        assertThat(findAnswer.get()).isSameAs(saveAnswer);
    }

    @Test
    @DisplayName("Answer 저장 후 Question 수정 테스트")
    void updateAnswer() {
        Answer saveAnswer = answerRepository.save(AnswerTest.A1);
        saveAnswer.toQuestion(QuestionTest.Q2);

        Optional<Answer> findAnswer = answerRepository.findById(saveAnswer.getId());

        assertThat(findAnswer).isPresent();
        assertThat(saveAnswer.getQuestionId()).isEqualTo(findAnswer.get().getQuestionId());
    }

    @Test
    @DisplayName("Answer 저장 후 Answer 삭제 테스트")
    void deleteAnswer() {
        Answer saveAnswer = answerRepository.save(AnswerTest.A1);
        answerRepository.delete(saveAnswer);

        Optional<Answer> findAnswer = answerRepository.findById(saveAnswer.getId());

        assertThat(findAnswer).isNotPresent();
    }

    @Test
    @DisplayName("Answer 저장 후 deleted가 false면 조회되는지 테스트")
    void findByIdAndDeletedFalse() {
        Answer saveAnswer = answerRepository.save(AnswerTest.A1);
        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(saveAnswer.getId());

        assertThat(findAnswer).isPresent();
        assertThat(saveAnswer).isEqualTo(findAnswer.get());
    }

    @Test
    @DisplayName("Answer 저장 후 deleted가 true면 조회되는지 테스트")
    void findByIdAndDeletedTrue() {
        Answer saveAnswer = answerRepository.save(AnswerTest.A1);
        saveAnswer.setDeleted(true);

        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(saveAnswer.getId());

        assertThat(findAnswer).isNotPresent();
    }

    @Test
    @DisplayName("Answer 저장 후 Question id로 조회되는지 테스트")
    void findByQuestionIdAndDeletedFalse() {
        Answer savedAnswer = answerRepository.save(AnswerTest.A1);
        List<Answer> findAnswer = answerRepository.findByQuestionIdAndDeletedFalse(savedAnswer.getQuestionId());

        assertThat(findAnswer).containsExactly(savedAnswer);
    }

    @Test
    @DisplayName("Answer 저장 후 delete가 true인 경우 Question id로 조회되는지 테스트")
    void findByQuestionIdAndDeletedTrue() {
        Answer savedAnswer = answerRepository.save(AnswerTest.A1);
        savedAnswer.setDeleted(true);

        List<Answer> findAnswer = answerRepository.findByQuestionIdAndDeletedFalse(savedAnswer.getQuestionId());

        assertThat(findAnswer).isEmpty();
    }
}
