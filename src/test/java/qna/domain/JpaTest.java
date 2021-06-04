package qna.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class JpaTest {

    @Autowired
    private AnswerRepository answers;

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private DeleteHistoryRepository deleteHistories;

    @Autowired
    private UserRepository users;

    public AnswerRepository getAnswers() {
        return answers;
    }

    public QuestionRepository getQuestions() {
        return questions;
    }

    public DeleteHistoryRepository getDeleteHistories() {
        return deleteHistories;
    }

    public UserRepository getUsers() {
        return users;
    }
}
