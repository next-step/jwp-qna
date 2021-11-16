package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.answer.Answer;
import qna.domain.answer.AnswerRepository;
import qna.domain.answer.AnswerTestFactory;
import qna.domain.deletehistory.ContentType;
import qna.domain.deletehistory.DeleteHistory;
import qna.domain.deletehistory.DeleteHistoryRepository;
import qna.domain.question.Question;
import qna.domain.question.QuestionRepository;
import qna.domain.user.User;
import qna.domain.user.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeleteHistoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("DeleteHistory 를 저장 할 경우 저장된 객체와 저장 후 객체가 일치하다")
    void save() {
        // given
        final User questionWriter = userRepository.save(UserTestFactory.create("testuser2", "testuser222@test.com"));
        final Question question = questionRepository.save(QuestionTestFactory.create(questionWriter));
        final User answerWriter = userRepository.save(UserTestFactory.create("testuser1", "testuser111@test.com"));
        final Answer savedAnswer = answerRepository.save(AnswerTestFactory.create(answerWriter, question, "Answer Content"));
        final User deletedUser = userRepository.save(UserTestFactory.create("doyoung", "doyoung@qna.test"));
        // given
        final DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, savedAnswer.getId(), deletedUser);
        // when
        final DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(deleteHistory);
        // then
        assertEquals(savedDeleteHistory, deleteHistory);
    }
}