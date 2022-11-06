package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.AnswerTest;
import qna.domain.Question;
import qna.domain.QuestionTest;
import qna.domain.User;
import qna.domain.UserTest;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("답변을 저장 후 확인")
    @Test
    void save() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(new Question("title", "contents").writeBy(user));
        Answer answer = answerRepository.save(new Answer(user, question, "contents"));

        Optional<Answer> result = answerRepository.findById(answer.getId());

        assertAll(
            () -> assertThat(result).isPresent(),
            () -> assertThat(result).get().isEqualTo(answer)
        );
    }

    @DisplayName("답변을 저장 후 조회 확인")
    @Test
    void findAll() {
        User user1 = userRepository.save(UserTest.JAVAJIGI);
        User user2 = userRepository.save(UserTest.SANJIGI);
        Question question1 = questionRepository.save(new Question("title", "contents").writeBy(user1));
        Question question2 = questionRepository.save(new Question("title", "contents").writeBy(user2));
        Answer answer1 = answerRepository.save(new Answer(user1, question1, "contents"));
        Answer answer2 = answerRepository.save(new Answer(user2, question2, "contents"));

        List<Answer> result = answerRepository.findAll();

        assertAll(
            () -> assertThat(result).hasSize(2),
            () -> assertThat(result).contains(answer1, answer2)
        );
    }

    @DisplayName("답변을 저장 후 수정 확인")
    @Test
    void update() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(new Question("title", "contents").writeBy(user));
        Answer answer = answerRepository.save(new Answer(user, question, "contents"));
        answer.setContents("update contents");

        Optional<Answer> result = answerRepository.findById(answer.getId());

        assertAll(
            () -> assertThat(result).isPresent(),
            () -> assertThat(result.get().getQuestion()).isEqualTo(answer.getQuestion())
        );
    }

    @DisplayName("답변을 저장 후 삭제 확인")
    @Test
    void remove() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(new Question("title", "contents").writeBy(user));
        Answer answer = answerRepository.save(new Answer(user, question, "contents"));
        answerRepository.delete(answer);

        Optional<Answer> result = answerRepository.findById(answer.getId());

        assertThat(result).isNotPresent();
    }

    @DisplayName("질문 식별자로 삭제되지 않는 답변을 조회")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(new Question("title", "contents").writeBy(user));
        Answer answer = answerRepository.save(new Answer(user, question, "contents"));

        List<Answer> result = answerRepository.findByQuestionIdAndDeletedFalse(
            answer.getQuestion().getId());

        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result).contains(answer),
            () -> assertThat(result.get(0)).isEqualTo(answer)
        );
    }

    @DisplayName("답변 식별자로 삭제되지 않는 답변을 조회")
    @Test
    void findByIdAndDeletedFalse() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(new Question("title", "contents").writeBy(user));
        Answer answer = answerRepository.save(new Answer(user, question, "contents"));

        Optional<Answer> result = answerRepository.findByIdAndDeletedFalse(answer.getId());

        assertAll(
            () -> assertThat(result.isPresent()).isTrue(),
            () -> assertThat(result.get()).isEqualTo(answer)
        );
    }
}
