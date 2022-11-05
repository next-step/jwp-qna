package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;
import qna.domain.UserTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("question save() 테스트를 진행한다")
    void saveQuestion() {
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

        Question result = questionRepository.save(question);

        assertThat(question).isEqualTo(result);

    }

    @Test
    @DisplayName("Question을 저장하고 데이터에 존재하는지 찾아본다")
    void saveQuestionAndFind() {
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        Question saveQuestion = questionRepository.save(question);
        saveQuestion.setDeleted(false);
        Optional<Question> result = questionRepository.findByIdAndDeletedFalse(saveQuestion.getId());

        assertThat(result.get()).isEqualTo(question);
    }

    @Test
    @DisplayName("Question이 삭제가 되는지 확인한다")
    void questionDelete() {
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

        Question saveQuestion = questionRepository.save(question);
        questionRepository.delete(saveQuestion);

        Optional<Question> findQuestion = questionRepository.findById(saveQuestion.getId());

        assertThat(findQuestion).isEmpty();
        assertThat(findQuestion).isNotPresent();
    }


    @Test
    @DisplayName("삭제된 question은 가져 올 수 없다.")
    void answerDeleteNotFind() {
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

        Question saveQuestion = questionRepository.save(question);
        saveQuestion.setDeleted(true);

        Optional<Question> result = questionRepository.findByIdAndDeletedFalse(saveQuestion.getId());

        assertThat(result).isEmpty();
        assertThat(result).isNotPresent();
    }

    @Test
    @DisplayName("삭제되지 않은 모든 Question 리스트를 불러온다 - 삭제된거 미포함")
    void findAllQuestionNotDeleted() {
        Question questionA = questionRepository.save(
                new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI));
        Question questionB = questionRepository.save(
                new Question("title2", "contents2").writeBy(UserTest.SANJIGI));

        List<Question> result = questionRepository.findByDeletedFalse();

        assertThat(result).hasSize(2);
        assertThat(result).contains(questionA, questionB);
    }

    @Test
    @DisplayName("삭제되지 않은 모든 Question 리스트를 불러온다 - 삭제된거 포함")
    void findAllQuestionNotDeletedContainDeleted() {
        Question questionA = questionRepository.save(
                new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI));
        Question questionB = questionRepository.save(
                new Question("title2", "contents2").writeBy(UserTest.SANJIGI));

        questionB.setDeleted(true);

        List<Question> result = questionRepository.findByDeletedFalse();


        assertThat(result).hasSize(1);
        assertThat(result).contains(questionA);
    }
}