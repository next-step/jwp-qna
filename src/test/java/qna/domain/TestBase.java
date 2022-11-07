package qna.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public abstract class TestBase {

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestEntityManager testEntityManager;

    protected void flushAndClear() {
        testEntityManager.flush();
        testEntityManager.clear();
    }
}
