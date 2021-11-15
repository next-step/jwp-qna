package qna;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.answer.Answer;
import qna.question.Question;
import qna.question.QuestionRepository;
import qna.user.User;
import qna.user.UserRepository;

import static qna.user.UserTest.JAVAJIGI;

@DataJpaTest
public class PreExecutionTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;

    public User savedUser;
    public Question savedQuestion;
    public Answer savedAnswer;

    @BeforeEach
    public void preExecution() {
        savedUser = userRepository.save(JAVAJIGI);
        savedQuestion = questionRepository.save(new Question("title1", "contents1", savedUser));
        savedAnswer = new Answer(savedUser, savedQuestion, "Answers Contents1");
    }
}
