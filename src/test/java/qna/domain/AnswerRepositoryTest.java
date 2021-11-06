package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private Answer saveAnswer(Answer answer, User user, Question question){

        User saveUser = userRepository.save(user);
        question.writeBy(saveUser);
        Question saveQuestion = questionRepository.save(question);
        Answer saveAnswer = answer;
        saveAnswer.setWriter(saveUser);
        saveAnswer.setQuestion(saveQuestion);
        saveAnswer = answerRepository.save(saveAnswer);

        return saveAnswer;
    }

    @DisplayName("ANSWER이 잘 저장되는지 확인한다.")
    @Test
    void saveAnswerTest() {

        Answer saveAnswer = saveAnswer(AnswerTest.A1, UserTest.JAVAJIGI, QuestionTest.Q1);

        assertThat(saveAnswer.getWriter()).isEqualTo(UserTest.JAVAJIGI);
    }

    @DisplayName("QUESTION ID로 삭제되지 않은 질문을 확인한다.")
    @Test
    void findByQuestionIdAndDeletedFalseTest() {

        Answer saveAnswer = saveAnswer(AnswerTest.A1, UserTest.JAVAJIGI, QuestionTest.Q1);

        assertEquals(1, answerRepository.findByQuestionIdAndDeletedFalse(saveAnswer.getQuestion().getId()).size());
    }

    @DisplayName("ANSWER ID로 삭제되지 않은 질문을 확인한다.")
    @Test
    void findByIdAndDeletedFalseTest() {

        Answer saveAnswer = saveAnswer(AnswerTest.A1, UserTest.JAVAJIGI, QuestionTest.Q1);

        assertSame(saveAnswer, answerRepository.findByIdAndDeletedFalse(saveAnswer.getId()).get());

    }
}
