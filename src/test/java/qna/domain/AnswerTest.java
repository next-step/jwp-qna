package qna.domain;

import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import qna.config.TestDataSourceConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@TestDataSourceConfig
public class AnswerTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private Question question;
    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("user", "pwd", "name", "email"));

        question = new Question(1L, "title1", "contents1").writeBy(user);
        question = questionRepository.save(question);
    }

    @DisplayName("Answer entity 저장 검증")
    @Test
    void saveTest() {

        Answer answer = new Answer(user, question, "contents");
        Answer saved = answerRepository.save(answer);
        assertNotNull(saved.getId());

        assertEquals(answer.getWriter(), saved.getWriter());
        assertEquals(answer.getQuestion(), saved.getQuestion());
    }

    @DisplayName("삭제되지 않은 데이터 찾아오기")
    @Test
    void findByIdAndDeletedFalseTest01() {

        Answer notDeletedAnswer = new Answer(user, question, "contents");

        Answer expected = answerRepository.save(notDeletedAnswer);
        Answer actual = answerRepository.findByIdAndDeletedFalse(expected.getId())
                                        .orElseThrow(EntityNotFoundException::new);

        assertFalse(actual.isDeleted());
        equals(expected, actual);
    }

    @DisplayName("삭제된 데이터는 찾아올 수 없음")
    @Test
    void findByIdAndDeletedFalseTest02() {

        Answer deletedAnswer = new Answer(user, question, "contents");
        deletedAnswer = answerRepository.save(deletedAnswer);
        deletedAnswer.delete();

        Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(deletedAnswer.getId());
        assertFalse(actual.isPresent());
    }

    private void equals(Answer expected, Answer actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getWriter(), actual.getWriter());
        assertEquals(expected.getQuestion(), actual.getQuestion());
        assertEquals(expected.getContents(), actual.getContents());
    }
}
