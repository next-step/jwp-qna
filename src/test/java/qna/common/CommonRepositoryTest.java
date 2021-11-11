package qna.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jdk.nashorn.internal.ir.annotations.Ignore;
import qna.domain.AnswerRepository;
import qna.domain.DeleteHistoryRepository;
import qna.domain.QuestionRepository;
import qna.domain.UserRepository;

@Ignore
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class CommonRepositoryTest {
    @Autowired
    protected DeleteHistoryRepository deleteHistoryRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected QuestionRepository questionRepository;
    @Autowired
    protected AnswerRepository answerRepository;
}
