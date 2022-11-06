package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class NewEntityTestBase {

    @Autowired
    protected AnswerRepository answerRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected QuestionRepository questionRepository;
    protected User NEWUSER1;
    protected User NEWUSER2;
    protected Question Q1;
    protected Question Q2;
    protected Answer A1;
    protected Answer A2;


    @BeforeEach
    void setUp() {
        NEWUSER1 = new User("id1", "pass", "name", "email");
        NEWUSER2 = new User("id2", "pass", "name", "email");
        Q1 = new Question("title", "contents", NEWUSER1);
        Q2 = new Question("title", "contents", NEWUSER2);
        A1 = new Answer(NEWUSER1, Q1, "Answers Contents1");
        A2 = new Answer(NEWUSER2, Q2, "Answers Contents2");
        userRepository.saveAll(Arrays.asList(NEWUSER1, NEWUSER2));
        questionRepository.saveAll(Arrays.asList(Q1, Q2));
        answerRepository.saveAll(Arrays.asList(A1, A2));
    }
}
