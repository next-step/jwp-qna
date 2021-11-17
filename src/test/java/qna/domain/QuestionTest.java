package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;
import qna.domain.answer.Answer;
import qna.domain.answer.AnswerRepository;
import qna.domain.answer.AnswerTestFactory;
import qna.domain.deletehistory.ContentType;
import qna.domain.deletehistory.DeleteHistory;
import qna.domain.question.Question;
import qna.domain.question.QuestionRepository;
import qna.domain.user.User;
import qna.domain.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
    public static final Question Q3 = new Question("title3", "contents3", true).writeBy(UserTest.SANJIGI);

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Question 을 저장 할 경우 저장된 객체와 저장 후 객체가 일치하다")
    void save() {
        // given
        final User questionWriter = userRepository.save(UserTestFactory.create("testuser2", "testuser222@test.com"));
        final Question question = QuestionTestFactory.create("title", "content", questionWriter);
        // when
        final Question savedQuestion = questionRepository.save(question);
        // then
        assertEquals(savedQuestion, question);
    }

    @Test
    @DisplayName("삭제되지 않은 Question 을 아이디를 통해 존재 여부를 확인 할 수 있다.")
    void existsById() {
        // given
        final User questionWriter = userRepository.save(UserTestFactory.create("testuser2", "testuser222@test.com"));
        final Question savedDeletedQuestion = questionRepository.save(QuestionTestFactory.create("title", "content", questionWriter, true));
        final Question savedNotDeletedQuestion = questionRepository.save(QuestionTestFactory.create("title2", "content2", questionWriter));
        // when
        final boolean isExistDeletedQuestion = questionRepository.existsById(savedDeletedQuestion.getId());
        final boolean isExistNotDeletedQuestion = questionRepository.existsById(savedNotDeletedQuestion.getId());
        // then
        assertAll(() -> {
            assertFalse(isExistDeletedQuestion);
            assertTrue(isExistNotDeletedQuestion);
        });
    }

    @Test
    @DisplayName("삭제되지 않은 Question 을 아이디를 통해 찾을 수 있다")
    void findById() {
        // given
        final User questionWriter = userRepository.save(UserTestFactory.create("testuser2", "testuser222@test.com"));
        final Question savedQuestion = questionRepository.save(QuestionTestFactory.create("title", "content", questionWriter, true));
        final Optional<Question> questionOptional = questionRepository.findById(savedQuestion.getId());
        assertAll(() -> {
            assertTrue(questionOptional.isPresent());
            assertEquals(questionOptional.get(), savedQuestion);
        });
    }

    @Test
    @DisplayName("삭제되지 않은 Question 의 리스트를 조회 할 수 있다.")
    void findAll() {
        // given
        final User questionWriter = userRepository.save(UserTestFactory.create("testuser2", "testuser222@test.com"));
        final Question savedDeletedQuestion = questionRepository.save(QuestionTestFactory.create("title", "content", questionWriter, true));
        final Question savedNotDeletedQuestion = questionRepository.save(QuestionTestFactory.create("title2", "content2", questionWriter));
        final Question savedNotDeletedQuestion2 = questionRepository.save(QuestionTestFactory.create("title3", "content3", questionWriter));

        final List<Question> foundQuestions = questionRepository.findAll();
        assertAll(() -> {
            assertTrue(foundQuestions.contains(savedNotDeletedQuestion));
            assertTrue(foundQuestions.contains(savedNotDeletedQuestion2));
            assertFalse(foundQuestions.contains(savedDeletedQuestion));
        });
    }

    @Test
    @DisplayName("Question 을 삭제 하면 삭제 기록과 삭제된 Question 을 조회 할 수 없다.")
    void delete() throws CannotDeleteException {
        // given
        final User writer = userRepository.save(UserTestFactory.create("testuser1", "testuser111@test.com"));
        final Question question = QuestionTestFactory.create("title", "content", writer);
        final Answer answer = AnswerTestFactory.create(writer, question, "Answer Content");
        final Answer answer2 = AnswerTestFactory.create(writer, question, "Answer Content2");
        question.addAnswer(answer);
        question.addAnswer(answer2);
        final Question savedQuestion = questionRepository.save(question);
        final Answer savedAnswer1 = savedQuestion.getAnswers().get(0);
        final Answer savedAnswer2 = savedQuestion.getAnswers().get(1);
        // when
        final List<DeleteHistory> deleteHistories = savedQuestion.deleteByUser(writer);
        // then
        assertAll(() -> {
            assertThat(deleteHistories).contains(DeleteHistory.ofQuestion(savedQuestion.getId(), writer));
            assertThat(deleteHistories).contains(DeleteHistory.ofAnswer(savedAnswer1.getId(), writer));
            assertThat(deleteHistories).contains(DeleteHistory.ofAnswer(savedAnswer2.getId(), writer));
            assertFalse(questionRepository.existsById(savedQuestion.getId()));
        });
    }


    @AfterEach
    void clear() {
        answerRepository.deleteAllInBatch();
        questionRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

}
