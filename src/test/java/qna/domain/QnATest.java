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

    protected static final String TITLE_1 = "title1";
    protected static final String TITLE_2 = "title2";
    protected static final String CONTENTS = "contents";
    protected static final String ANSWER = "답글";

    protected Question createQuestions(User user, String title) {
        return questionRepository.save(new Question(title, CONTENTS).writeBy(user));
    }

    protected List<Question> createQuestions(User user, String... titles) {
        List<Question> questions = stream(titles)
            .map(title -> new Question(title, CONTENTS).writeBy(user))
            .collect(toList());
        return questionRepository.saveAll(questions);
    }

    protected List<Answer> createAnswers(User writer, Question question) {
        return answerRepository.saveAll(asList(
            new Answer(writer, question, CONTENTS),
            new Answer(writer, question, CONTENTS)
        ));
    }

    protected List<User> createUsers() {
        return userRepository.saveAll(asList(
            new User("userId1", "password", "userName2", "userName1@gmail.com"),
            new User("userId2", "password", "userName2", "userName2@gmail.com")));
    }
}
