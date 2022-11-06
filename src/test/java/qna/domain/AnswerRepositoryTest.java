package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DisplayName("답변 테스트")
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("데이터 저장 확인")
    void save() {
        Question question = questionRepository.save(QuestionTest.Q3);

        Answer answer = new Answer(UserTest.JAVAJIGI, question, "contents");
        Answer savedAnswer = answerRepository.save(answer);

        assertThat(savedAnswer).isEqualTo(answer);
        assertThat(savedAnswer.getQuestion()).isEqualTo(question);
    }

    @Test
    @DisplayName("저장한 답변과 해당 답변이 같은지 확인")
    void read() {
        Question question = questionRepository.save(QuestionTest.Q3);

        Answer answer = new Answer(UserTest.JAVAJIGI, question, "contents");
        Answer savedAnswer = answerRepository.save(answer);

        Optional<Answer> findAnswer = answerRepository.findById(savedAnswer.getId());

        assertThat(savedAnswer).isEqualTo(findAnswer.get());
    }

    @Test
    @DisplayName("저장한 답변의 질문 변경 시 일치 여부 확인")
    void update() {
        Question question = questionRepository.save(QuestionTest.Q3);

        Answer answer = new Answer(UserTest.JAVAJIGI, question, "contents");
        Answer savedAnswer = answerRepository.save(answer);

        savedAnswer.toQuestion(QuestionTest.Q4);

        Optional<Answer> findAnswer = answerRepository.findById(savedAnswer.getId());

        assertThat(savedAnswer.getQuestion()).isEqualTo(findAnswer.get().getQuestion());
    }

    @Test
    @DisplayName("저장한 답변 삭제 확인")
    void delete() {
        Question question = questionRepository.save(QuestionTest.Q3);

        Answer answer = new Answer(UserTest.JAVAJIGI, question, "contents");
        Answer savedAnswer = answerRepository.save(answer);

        answerRepository.delete(savedAnswer);

        Optional<Answer> findAnswer = answerRepository.findById(savedAnswer.getId());

        assertThat(findAnswer).isEmpty();
        assertThat(findAnswer).isNotPresent();
    }

    @Test
    @DisplayName("답변 삭제 불가 확인")
    void answer_cannot_deleted() {
        Question question = questionRepository.save(QuestionTest.Q3);

        Answer answer = new Answer(UserTest.JAVAJIGI, question, "contents");
        Answer savedAnswer = answerRepository.save(answer);

        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId());

        assertThat(savedAnswer).isEqualTo(findAnswer.get());
    }

    @Test
    @DisplayName("답변 삭제 확인")
    void answer_can_deleted() {
        Question question = questionRepository.save(QuestionTest.Q3);

        Answer answer = new Answer(UserTest.JAVAJIGI, question, "contents");
        Answer savedAnswer = answerRepository.save(answer);

        savedAnswer.setDeleted(true);

        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId());

        assertThat(findAnswer).isEmpty();
    }

    @Test
    @DisplayName("답변 false 개수 확인 1")
    void find_answer_not_deleted_count() {
        Question question = questionRepository.save(QuestionTest.Q3);

        Answer answer1 = new Answer(UserTest.JAVAJIGI, question, "contents");
        Answer savedAnswer1 = answerRepository.save(answer1);
        savedAnswer1.setDeleted(false);

        Answer answer2 = new Answer(UserTest.JAVAJIGI, question, "contents");
        Answer savedAnswer2 = answerRepository.save(answer2);
        savedAnswer2.setDeleted(false);

        List<Answer> findAnswers = answerRepository.findByQuestionAndDeletedFalse(question);

        assertThat(findAnswers).contains(savedAnswer1, savedAnswer2);
        assertThat(findAnswers).hasSize(2);

    }

    @Test
    @DisplayName("답변 false 개수 확인 2")
    void find_answer_not_deleted_count_2() {
        Question question = questionRepository.save(QuestionTest.Q3);

        Answer answer1 = new Answer(UserTest.JAVAJIGI, question, "contents");
        Answer savedAnswer1 = answerRepository.save(answer1);
        savedAnswer1.setDeleted(false);

        Answer answer2 = new Answer(UserTest.JAVAJIGI, question, "contents");
        Answer savedAnswer2 = answerRepository.save(answer2);
        savedAnswer2.setDeleted(true);

        List<Answer> findAnswers = answerRepository.findByQuestionAndDeletedFalse(question);

        assertThat(findAnswers).containsExactlyInAnyOrder(savedAnswer1);
        assertThat(findAnswers).hasSize(1);
    }

}
