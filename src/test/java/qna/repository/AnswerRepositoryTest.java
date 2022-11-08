package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.QuestionTest;
import qna.domain.User;
import qna.domain.UserTest;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;


    @DisplayName("Answer을 저장할 수 있다.")
    @Test
    void save() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        Answer actual = answerRepository.save(answer);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriter()).isEqualTo(UserTest.JAVAJIGI),
                () -> assertThat(actual.getQuestion()).isEqualTo(QuestionTest.Q1),
                () -> assertThat(actual.getContents()).isEqualTo(answer.getContents())
        );
    }

    @DisplayName("저장된 Answer을 조회할 수 있다.")
    @Test
    void find() {
        Answer actual = answerRepository.save(new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents"));

        Optional<Answer> findAnswer = answerRepository.findById(actual.getId());

        assertThat(findAnswer).isPresent();
    }

    @DisplayName("저장된 Answer을 삭제할 수 있다.")
    @Test
    void delete() {
        Answer actual = answerRepository.save(new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents"));

        answerRepository.delete(actual);
        Optional<Answer> findAnswer = answerRepository.findById(actual.getId());

        assertThat(findAnswer).isNotPresent();
    }

    @DisplayName("저장된 Answer의 값을 변경할 수 있다.")
    @Test
    void update() {
        Answer actual = answerRepository.save(new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents"));

        actual.toQuestion(QuestionTest.Q2);
        Optional<Answer> findAnswer = answerRepository.findById(actual.getId());

        assertThat(findAnswer.orElseThrow(NotFoundException::new).getQuestion()).isEqualTo(QuestionTest.Q2);

    }

    @DisplayName("questionId로 저장된 Answer 중 삭제되지 않은 것을 조회할 수 있다.")
    @Test
    void find_by_question_id_and_deleted_false() {
        Question question = questionRepository.save(new Question("title", "contents"));
        User user = userRepository.save(new User("test", "password", "name", "test@kr.com"));
        Answer answerA1 = answerRepository.save(new Answer(user, question, ""));
        Answer answerA2 = answerRepository.save(new Answer(user, question, ""));

        answerA1.makeDeletedTrue();
        List<Answer> expect = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());

        assertThat(expect).containsExactly(answerA2);
    }

    @DisplayName("id로 저장된 Answer 중 삭제되지 않은 것을 조회할 수 있다.")
    @Test
    void find_by_id_and_deleted_false() {
        Question question = questionRepository.save(new Question("title", "contents"));
        User user = userRepository.save(new User("test", "password", "name", "test@kr.com"));
        Answer actual1 = answerRepository.save(new Answer(user, question, ""));
        Answer actual2 = answerRepository.save(new Answer(user, question, ""));

        actual2.makeDeletedTrue();
        Optional<Answer> expect1 = answerRepository.findByIdAndDeletedFalse(actual1.getId());
        Optional<Answer> expect2 = answerRepository.findByIdAndDeletedFalse(actual2.getId());

        assertAll(
                () -> assertThat(expect1).isPresent(),
                () -> assertThat(expect2).isNotPresent()
        );

    }
}
