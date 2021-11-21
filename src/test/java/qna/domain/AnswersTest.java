package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.AnswerTest.answer;
import static qna.domain.QuestionTest.question;
import static qna.domain.UserTest.userB;

@DataJpaTest
public class AnswersTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("Answers 일급 콜렉션 생성")
    @Test
    void createAnswers() throws CannotDeleteException {
        Answer answer = answer(5);
        answer.mappingToWriter(userRepository.save(userB()));
        answer.mappingToQuestion(questionRepository.save(question(5)));
        Answer save = answerRepository.save(answer);
        Answers answers = Answers.createAnswers(answerRepository.findByQuestionIdAndDeletedFalse(save.getQuestion().getId()), save.getWriter());

    }

    @DisplayName("답변 작성자와 로그인 유저가 다르면 CannotDeleteException 에러 발생")
    @Test
    void validateAnswerWriterSameAsLoginUser() {
        Answer answer = answer(5);
        answer.mappingToWriter(userRepository.save(userB()));
        answer.mappingToQuestion(questionRepository.save(question(5)));
        Answer save = answerRepository.save(answer);

        assertThatThrownBy(()->
                Answers.createAnswers(answerRepository.findByQuestionIdAndDeletedFalse(save.getQuestion().getId()), userB())
                    ).isInstanceOf(CannotDeleteException.class).hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
