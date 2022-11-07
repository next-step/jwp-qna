package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    QuestionRepository questionRepository;
    User savedUser;
    Question savedQuestion;

    @BeforeEach
    void setUp() {
        savedUser = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        savedQuestion = questionRepository.save(new Question("title1", "contents1")
                .writeBy(savedUser));
    }

    @Test
    @DisplayName("questionId와 일치하고 삭제상태가 false인 Answer목록을 반환")
    void test_returns_answers_with_questionId_and_deleted_is_false() {
        Answer savedAnswer = answerRepository.save(new Answer(savedUser, savedQuestion, "contents"));

        List<Answer> findAnswers = answerRepository.findByQuestionIdAndDeletedFalse(savedAnswer.getQuestion().getId());

        assertAll(
                () -> assertThat(findAnswers).hasSize(1),
                () -> assertThat(findAnswers).contains(savedAnswer)
        );
    }

    @Test
    @DisplayName("questionId가 일치하지 않으면 빈값을 반환")
    void test_returns_empty_when_not_equal_questionid() {
        Question question = questionRepository.save(new Question("title1", "contents1")
                .writeBy(savedUser));
        answerRepository.save(new Answer(savedUser, savedQuestion, "contents"));
        List<Answer> findAnswers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());

        assertThat(findAnswers).isEmpty();
    }

    @Test
    @DisplayName("Answer의 id와 일치하고 삭제상태가 false인 Answer를 반환")
    void test_returns_answer_with_answerId_and_deleted_is_false() {
        Answer savedAnswer = answerRepository.save(new Answer(savedUser, savedQuestion, "contents"));

        Optional<Answer> answer = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId());

        assertThat(answer).contains(savedAnswer);
    }

    @Test
    @DisplayName("user의 id와 일치하고 삭제상태가 false인 Answer를 반환")
    void test_returns_answer_with_userId_and_deleted_is_false() {
        Answer savedAnswer = answerRepository.save(new Answer(savedUser, savedQuestion, "contents"));

        List<Answer> findAnswers = answerRepository.findByWriterIdAndDeletedFalse(savedAnswer.getWriter().getId());

        assertAll(
                () -> assertThat(findAnswers).hasSize(1),
                () -> assertThat(findAnswers).contains(savedAnswer)
        );
    }

    @Test
    @DisplayName("userId가 일치하지 않으면 빈값을 반환")
    void test_returns_empty_when_not_equal_userid() {
        User user = userRepository.save(new User("mink", "password", "name", "mink@slipp.net"));
        answerRepository.save(new Answer(savedUser, savedQuestion, "contents"));

        List<Answer> answer = answerRepository.findByWriterIdAndDeletedFalse(user.getId());

        assertThat(answer).isEmpty();
    }

}
