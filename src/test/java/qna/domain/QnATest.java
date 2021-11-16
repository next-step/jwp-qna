package qna.domain;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class QnATest {

    @Autowired
    protected QuestionRepository questionRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected AnswerRepository answerRepository;

    @PersistenceContext
    EntityManager entityManager;
    protected static final String USER_A = "userA";
    protected static final String USER_A_EMAIL = "userA@gmail.com";
    protected static final String USER_B = "userB";
    protected static final String USER_B_EMAIL = "userB@gmail.com";
    protected static final String PASSWORD = "password";
    protected static final String TITLE_1 = "title1";
    protected static final String TITLE_2 = "title2";
    protected static final String CONTENTS_1 = "contents1";
    protected static final String CONTENTS_2 = "contents2";
    protected static final String CONTENTS_3 = "contents3";
    protected static final String ANSWER_1 = "답글1";
    protected static final String ANSWER_2 = "답글2";

    protected Question createQuestion(User user, String title, String content) {
        return questionRepository.save(new Question(title, content).writeBy(user));
    }

    protected Answer createAnswer(User writer, Question question,String answerContent) {
        return answerRepository.save(new Answer(writer, question, answerContent));
    }

    protected User createUser(String id, String password, String userName, String email) {
        return userRepository.save(new User(id, password, userName, email));
    }
    protected User createUser() {
        return userRepository.save(new User(USER_A, PASSWORD, USER_A, USER_A));
    }

}
