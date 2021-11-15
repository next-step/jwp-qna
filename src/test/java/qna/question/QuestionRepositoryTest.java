package qna.question;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;
import qna.PreExecutionTest;
import qna.user.User;
import qna.user.UserId;
import qna.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class QuestionRepositoryTest extends PreExecutionTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository users;

    private Question savedQuestion;

    @BeforeEach
    private void beforeEach() {
        savedQuestion = questionRepository.save(new Question("title1", "contents1", savedUser));
    }

    @Test
    @DisplayName("question 등록")
    public void saveQuestionTest() {
        Question question = questionRepository.findById(savedQuestion.getId()).get();

        assertAll(
                () -> assertNotNull(savedQuestion.getId()),
                () -> assertEquals(question, savedQuestion)
        );
    }

    @Test
    @DisplayName("deleted가 false인 question 검색")
    public void findByDeletedFalseTest() {
        List<Question> questions = questionRepository.findByDeletedFalse();

        assertAll(
                () -> assertThat(questions).contains(savedQuestion)
        );
    }

    @Test
    @DisplayName("question id로 deleted가 false인 question 단일검색")
    public void findByIdAndDeletedFalseTest() {
        Optional<Question> oQuestion = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId());

        assertQuestionDeleted(oQuestion, false);

    }

    @Test
    @DisplayName("question에 delete를 true로 수정")
    public void updateQuestionDeletedTrue() throws CannotDeleteException {
        savedQuestion.delete(savedUser);

        Optional<Question> oQuestion = questionRepository.findById(savedQuestion.getId());

        assertQuestionDeleted(oQuestion, true);
    }

    @Test
    @DisplayName("user객체로 question 검색")
    public void findByUser() {
        User user = users.findByUserId(new UserId("javajigi")).get();

        List<Question> actual = questionRepository.findByUser(user);

        assertAll(
                () -> assertThat(actual).contains(savedQuestion)
        );
    }

    private void assertQuestionDeleted(Optional<Question> oAnswer, boolean isDeleted) {
        assertAll(
                () -> assertEquals(oAnswer.get(), savedQuestion),
                () -> assertThat(oAnswer.get().isDeleted()).isEqualTo(isDeleted)
        );
    }
}