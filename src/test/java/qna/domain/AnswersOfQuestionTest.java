package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;
import qna.error.ErrorMessage;

@DataJpaTest
public class AnswersOfQuestionTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    User user1;
    User user2;
    Question question;

    @BeforeEach
    void beforeEach(){
        user1 = userRepository.save(new User("user1", "user1!", "사용자1", ""));
        user2 = userRepository.save(new User("user2", "user2!", "사용자2", ""));
        question = questionRepository.save(new Question("질문 1", "본문 1").writeBy(user1));
    }

    @Test
    @DisplayName("답변 삭제 성공 테스트")
    void generateDeleteHistoriesTest() throws CannotDeleteException {
        answerRepository.save(new Answer(user1, question, "내용1"));
        answerRepository.save(new Answer(user1, question, "내용2"));

        AnswersOfQuestion answersOfQuestion = new AnswersOfQuestion(question.getAnswers());
        List<DeleteHistory> deleteHistories = answersOfQuestion.generateDeleteHistories(user1);

        assertThat(deleteHistories).hasSize(2);
    }

    @Test
    @DisplayName("답변 삭제 예외 테스트 - 다른 사람이 작성한 답변이 존재합니다.")
    void deleteHistoriesExceptionTest() throws CannotDeleteException{
        answerRepository.save(new Answer(user1, question, "내용1"));
        answerRepository.save(new Answer(user2, question, "내용2"));

        AnswersOfQuestion answersOfQuestion = new AnswersOfQuestion(question.getAnswers());

        assertThatThrownBy(
                () -> answersOfQuestion.generateDeleteHistories(user1)
        ).isInstanceOf(CannotDeleteException.class)
                .withFailMessage(ErrorMessage.CANT_DELETE_ANSWER_IF_EXISTS_OTHER_WRITER.message());
    }
}
