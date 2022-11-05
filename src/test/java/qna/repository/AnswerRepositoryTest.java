package qna.repository;

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
import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    QuestionRepository questionRepository;

    @Test
    @DisplayName("questionId와 일치하고 삭제상태가 false인 Answer목록을 반환")
    void test_returns_answers_with_questionId_and_deleted_is_false() {
        User user = userRepository.save(JAVAJIGI);
        Question question = questionRepository.save(Q1);
        Answer savedAnswer = answerRepository.save(new Answer(user, question, "contents"));

        List<Answer> findAnswers = answerRepository.findByQuestionIdAndDeletedFalse(savedAnswer.getQuestion().getId());

        assertAll(
                () -> assertThat(findAnswers.size()).isEqualTo(1),
                () -> assertThat(findAnswers).contains(savedAnswer)
        );
    }

    @Test
    @DisplayName("Answer의 id와 일치하고 삭제상태가 false인 Answer를 반환")
    void test_returns_answer_with_answerId_and_deleted_is_false() {
        User user = userRepository.save(JAVAJIGI);
        Question question = questionRepository.save(Q1);
        Answer savedAnswer = answerRepository.save(new Answer(user, question, "contents"));

        Optional<Answer> answer = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId());

        assertThat(answer).contains(savedAnswer);
    }

}
