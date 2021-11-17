package qna.domain.answer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.QuestionTestFactory;
import qna.domain.UserTestFactory;
import qna.domain.deletehistory.ContentType;
import qna.domain.deletehistory.DeleteHistory;
import qna.domain.question.Question;
import qna.domain.question.QuestionRepository;
import qna.domain.user.User;
import qna.domain.user.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AnswerTest {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("Answer 를 저장 할 경우 저장된 객체와 저장 후 객체가 일치하다")
    void save() {
        // given
        final User questionWriter = userRepository.save(UserTestFactory.create("testuser2", "testuser222@test.com"));
        final Question question = questionRepository.save(QuestionTestFactory.create(questionWriter));
        final User answerWriter = userRepository.save(UserTestFactory.create("testuser1", "testuser111@test.com"));
        final Answer answer = AnswerTestFactory.create(answerWriter, question, "Answer Content");
        // when
        final Answer savedAnswer = answerRepository.save(answer);
        // then
        assertTrue(savedAnswer.matchContent("Answer Content"));
        assertTrue(savedAnswer.getWriter().equalsNameAndEmail(answerWriter));
        assertEquals(savedAnswer.getContents(), "Answer Content");
        assertEquals(savedAnswer.getContents(), "Answer Content");
    }

    @Test
    @DisplayName("삭제되지 않은 Answer 를 아이디를 통해 찾을 수 있다")
    void findById() {
        // given
        final User questionWriter = userRepository.save(UserTestFactory.create("testuser2", "testuser222@test.com"));
        final Question question = questionRepository.save(QuestionTestFactory.create(questionWriter));
        final User answerWriter = userRepository.save(UserTestFactory.create("testuser1", "testuser111@test.com"));
        final Answer answer = AnswerTestFactory.create(answerWriter, question, "Answer Content");
        // when
        final Answer savedAnswer = answerRepository.save(answer);

        final Optional<Answer> answerOptional = answerRepository.findById(savedAnswer.getId());
        assertAll(() -> {
            assertTrue(answerOptional.isPresent());
            assertEquals(answerOptional.get(), savedAnswer);
        });
    }

    @Test
    @DisplayName("삭제되지 않은 Answer 를 Question 의 아이디를 통해 찾을 수 있다")
    void findByQuestionId() {
        // given
        final User questionWriter = userRepository.save(UserTestFactory.create("testuser2", "testuser222@test.com"));
        final Question question = questionRepository.save(QuestionTestFactory.create(questionWriter));
        final User answerWriter = userRepository.save(UserTestFactory.create("testuser1", "testuser111@test.com"));
        final Answer notDeletedAnswer1 = AnswerTestFactory.create(answerWriter, question, "Answer Content");
        final Answer notDeletedAnswer2 = AnswerTestFactory.create(answerWriter, question, "Answer Content2");
        final Answer deletedAnswer = AnswerTestFactory.create(answerWriter, question, "Answer Content3", true);
        answerRepository.saveAll(
                Arrays.asList(
                        notDeletedAnswer1,
                        notDeletedAnswer2,
                        deletedAnswer
                )
        );
        // when
        final List<Answer> foundAnswers = answerRepository.findByQuestionId(question.getId());
        // then
        assertAll(() -> {
            assertTrue(foundAnswers.contains(notDeletedAnswer1));
            assertTrue(foundAnswers.contains(notDeletedAnswer2));
            assertFalse(foundAnswers.contains(deletedAnswer));
            assertThat(foundAnswers).containsAll(Arrays.asList(notDeletedAnswer1, notDeletedAnswer2));
        });
    }

    @Test
    @DisplayName("answer 에 있는 user 의 상태가 변경되었을때 DB도 변경되는지 확인")
    void updateWithUser() {
        // given
        final User questionWriter = userRepository.save(UserTestFactory.create("testuser2", "testuser222@test.com"));
        final Question question = questionRepository.save(QuestionTestFactory.create(questionWriter));
        final User answerWriter = userRepository.save(UserTestFactory.create("testuser1", "testuser111@test.com"));
        final Answer savedAnswer = answerRepository.save(AnswerTestFactory.create(answerWriter, question, "Answer Content"));
        final Answer answer = answerRepository.findById(savedAnswer.getId()).get();
        final User newUser = UserTestFactory.create(answerWriter.getId(), answerWriter.getUserId(), "newEmail@email.com");
        // when
        answer.updateWriter(newUser);
        answerRepository.flush();
        answer.userClear();
        answerRepository.flush();
        // then
        assertThat(answerRepository.findById(savedAnswer.getId()).get().getWriter()).isNull();
        final User foundUser = userRepository.findByUserId(answerWriter.getUserId()).get();
        assertThat(foundUser.matchEmail("newEmail@email.com")).isTrue();
    }

    @Test
    @DisplayName("자신이 쓴 답변을 삭제하고 삭제 기록을 반환 받을 수 있다.")
    void deleteByUser() {
        // given
        final User writer = userRepository.save(UserTestFactory.create("testuser1", "testuser111@test.com"));
        final Question question = questionRepository.save(QuestionTestFactory.create("title", "content", writer));
        final Answer answer = AnswerTestFactory.create(writer, question, "Answer Content");
        answerRepository.save(answer);
        // when
        final DeleteHistory deleteHistory = answer.deleteByUser(writer);
        // then
        assertAll(() -> {
            assertTrue(deleteHistory.matchContentId(answer.getId()));
            assertTrue(deleteHistory.matchContentType(ContentType.ANSWER));
            assertTrue(deleteHistory.matchDeletedUser(writer));
        });
    }

    @AfterEach
    void clear() {
        answerRepository.deleteAllInBatch();
        questionRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }
}
