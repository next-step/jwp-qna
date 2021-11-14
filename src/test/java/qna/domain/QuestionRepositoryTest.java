package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.question.Question;
import qna.question.QuestionRepository;
import qna.user.User;
import qna.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;
import static qna.domain.UserTest.JAVAJIGI;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository users;

    private Question savedQuestion;

    @BeforeEach
    private void beforeEach() {
        User savedUser = users.save(JAVAJIGI);
        savedQuestion = questionRepository.save(Q1.writeBy(savedUser));
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
    public void updateQuestionDeletedTrue() {
        savedQuestion.deleteContent();

        Optional<Question> oQuestion = questionRepository.findById(savedQuestion.getId());

        assertQuestionDeleted(oQuestion, true);
    }

    @Test
    public void findByUser() {
        User user = users.findByUserId("javajigi").get();
        Question excepted = questionRepository.save(Q2.writeBy(user));

        List<Question> actual = questionRepository.findByUser(user);

        assertAll(
                () -> assertThat(actual).contains(excepted, savedQuestion)
        );
    }

    private void assertQuestionDeleted(Optional<Question> oAnswer, boolean isDeleted) {
        assertAll(
                () -> assertEquals(oAnswer.get(), savedQuestion),
                () -> assertThat(oAnswer.get().isDeleted()).isEqualTo(isDeleted)
        );
    }
}