package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    UserRepository userRepository;
    User user;

    @Autowired
    QuestionRepository questionRepository;
    Question question;

    @BeforeEach
    void before() {
        user = userRepository.save(new User("id", "password", "name", "email@kr.com"));
        question = questionRepository.save(new Question("title", "contents"));
    }

    @DisplayName("Answer을 저장할 수 있다.")
    @Test
    void save() {
        Answer answer = new Answer(user, question, "contents");
        Answer actual = answerRepository.save(answer);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriter()).isEqualTo(user),
                () -> assertThat(actual.getQuestion()).isEqualTo(question),
                () -> assertThat(actual.getContents()).isEqualTo(answer.getContents())
        );
    }

    @DisplayName("저장된 Answer을 조회할 수 있다.")
    @Test
    void find() {
        Answer actual = answerRepository.save(new Answer(user, question, "contents"));

        Optional<Answer> findAnswer = answerRepository.findById(actual.getId());

        assertThat(findAnswer).isPresent();
    }

    @DisplayName("저장된 Answer을 삭제할 수 있다.")
    @Test
    void delete() {
        Answer actual = answerRepository.save(new Answer(user, question, "contents"));

        answerRepository.delete(actual);
        Optional<Answer> findAnswer = answerRepository.findById(actual.getId());

        assertThat(findAnswer).isNotPresent();
    }

    @DisplayName("저장된 Answer의 값을 변경할 수 있다.")
    @Test
    void update() {
        Answer actual = answerRepository.save(new Answer(user, question, "contents"));
        Question newQuestion = questionRepository.save(new Question("newTitle", "newContets"));

        actual.toQuestion(newQuestion);
        Optional<Answer> findAnswer = answerRepository.findById(actual.getId());

        assertThat(findAnswer.orElseThrow(NotFoundException::new).getQuestion()).isEqualTo(newQuestion);

    }

    @DisplayName("questionId로 저장된 Answer 중 삭제되지 않은 것을 조회할 수 있다.")
    @Test
    void find_by_question_id_and_deleted_false() {
        Answer answerA1 = answerRepository.save(new Answer(user, question, ""));
        Answer answerA2 = answerRepository.save(new Answer(user, question, ""));

        answerA1.setDeleted(true);
        List<Answer> expect = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());

        assertThat(expect).containsExactly(answerA2);
    }

    @DisplayName("id로 저장된 Answer 중 삭제되지 않은 것을 조회할 수 있다.")
    @Test
    void find_by_id_and_deleted_false() {
        Answer actual1 = answerRepository.save(new Answer(user, question, ""));
        Answer actual2 = answerRepository.save(new Answer(user, question, ""));

        actual2.setDeleted(true);
        Optional<Answer> expect1 = answerRepository.findByIdAndDeletedFalse(actual1.getId());
        Optional<Answer> expect2 = answerRepository.findByIdAndDeletedFalse(actual2.getId());

        assertAll(
                () -> assertThat(expect1).isPresent(),
                () -> assertThat(expect2).isNotPresent()
        );

    }
}
