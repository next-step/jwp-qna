package qna.domain.collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;
import qna.domain.repository.AnswerRepository;
import qna.domain.repository.QuestionRepository;
import qna.domain.repository.UserRepository;
import qna.exception.CannotDeleteException;

@DataJpaTest
class AnswersTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private EntityManager entityManager;


    @DisplayName("질문 작성자와 답변들의 작성자가 모두 동일할 경우 전체 답변을 삭제한다.")
    @Test
    void deleteAll() throws CannotDeleteException {
        long questionId = 4L;
        long writerId = 3L;
        Question question = questionRepository.findById(questionId).get();
        User writer = userRepository.findById(writerId).get();
        Answers answers = question.getAnswers();

        answers.deleteAll(writer);
        entityManager.flush();
        entityManager.clear();

        List<Answer> actual = answerRepository.findByQuestionIdAndDeletedFalse(questionId);
        assertThat(actual).isEmpty();
    }

    @DisplayName("질문 작성자와 답변 작성자가 다른 경우 삭제할 수 없다.")
    @Test
    void deleteAll_not_question_writer()  {
        long questionId = 1L;
        long writerId = 1L;
        Question question = questionRepository.findById(questionId).get();
        User questionWriter = userRepository.findById(writerId).get();
        Answers answers = question.getAnswers();

        assertThatThrownBy(()-> answers.deleteAll(questionWriter))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("[ERROR] 다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");

    }
}
