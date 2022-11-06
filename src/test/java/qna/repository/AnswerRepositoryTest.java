package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("answer save() 테스트를 진행한다")
    void saveAnswer() {
        User user = userRepository.save(new User(1L, "javajigi", "password", "name", "javajigi@slipp.net"));
        Answer answer = new Answer(user, new Question("title1", "contents1").writeBy(user), "Answers Contents1");
        Answer saveAnswer = answerRepository.save(answer);

        assertThat(answer).isEqualTo(saveAnswer);
    }

    @Test
    @DisplayName("answer을 저장하고 데이터에 존재하는지 찾아본다")
    void saveAnswerAndFind() {
        User user = userRepository.save(new User(1L, "javajigi", "password", "name", "javajigi@slipp.net"));
        Question question = questionRepository.save(new Question("title1", "contents1").writeBy(user));
        Answer saveAnswer = answerRepository.save(new Answer(user, question, "Answers Contents1"));
        saveAnswer.setDeleted(false);

        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(saveAnswer.getId());

        assertThat(findAnswer).get().isEqualTo(saveAnswer);
    }

    @Test
    @DisplayName("answer가 삭제가 되는지 확인한다")
    void answerDelete() {
        User user = userRepository.save(new User(1L, "javajigi", "password", "name", "javajigi@slipp.net"));
        Question question = questionRepository.save(new Question("title1", "contents1").writeBy(user));
        Answer saveAnswer = answerRepository.save(new Answer(user, question, "Answers Contents1"));
        answerRepository.delete(saveAnswer);

        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(saveAnswer.getId());

        assertThat(findAnswer).isEmpty();
        assertThat(findAnswer).isNotPresent();
    }

    @Test
    @DisplayName("삭제된 answer는 가져 올 수 없다.")
    void answerDeleteNotFind() {
        User user = userRepository.save(new User(1L, "javajigi", "password", "name", "javajigi@slipp.net"));
        Question question = questionRepository.save(new Question("title1", "contents1").writeBy(user));
        Answer saveAnswer = answerRepository.save(new Answer(user, question, "Answers Contents1"));
        saveAnswer.setDeleted(true);

        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(saveAnswer.getId());

        assertThat(findAnswer).isEmpty();
        assertThat(findAnswer).isNotPresent();
    }

    @Test
    @DisplayName("QuestionId로 answer 리스트를 불러온다")
    void answerListByQuestionId() {
        User user = userRepository.save(new User(1L, "javajigi", "password", "name", "javajigi@slipp.net"));
        Question question = questionRepository.save(new Question("title1", "contents1").writeBy(user));
        Answer answerA = answerRepository.save(new Answer(user, question, "Answers Contents1"));
        answerA.setDeleted(false);

        Answer answerB = answerRepository.save(new Answer(user, question, "Answers Contents1"));
        answerB.setDeleted(false);

        List<Answer> result = answerRepository.findByQuestionAndDeletedFalse(answerB.getQuestion());

        assertThat(result).hasSize(2);
        assertThat(result).contains(answerA, answerB);
    }

    @Test
    @DisplayName("QuestionId로 answer 리스트를 불러온다 (아무것도 없을경우)")
    void answerListByQuestionIdNotResult() {
        User user = userRepository.save(new User(1L, "javajigi", "password", "name", "javajigi@slipp.net"));
        Question question = questionRepository.save(new Question("title1", "contents1").writeBy(user));
        Answer answerA = answerRepository.save(new Answer(user, question, "Answers Contents1"));
        answerA.setDeleted(true);

        Answer answerB = answerRepository.save(new Answer(user, question, "Answers Contents2"));
        answerB.setDeleted(true);

        List<Answer> findAnswers = answerRepository.findByQuestionAndDeletedFalse(answerB.getQuestion());

        assertThat(findAnswers).doesNotContain(answerA, answerB);
        assertThat(findAnswers).hasSize(0);
    }
}