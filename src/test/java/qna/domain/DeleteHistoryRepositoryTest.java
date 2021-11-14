package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.answer.Answer;
import qna.answer.AnswerRepository;
import qna.deletehistory.DeleteHistory;
import qna.deletehistory.DeleteHistoryRepository;
import qna.question.Question;
import qna.question.QuestionRepository;
import qna.user.User;
import qna.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.UserTest.JAVAJIGI;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    private DeleteHistory answerSavedDeleteHistory;
    private DeleteHistory questionSavedDeleteHistory;

    @BeforeEach
    private void beforeEach() {
        User javajigi = userRepository.save(JAVAJIGI);
        Question question = questionRepository.save(new Question("title1", "contents1", javajigi));
        Answer answer = answerRepository.save(new Answer(javajigi, question, "Answers Contents1"));

        answerSavedDeleteHistory = deleteHistoryRepository.save(new DeleteHistory(1L, ContentType.ANSWER, answer.getId(), javajigi));
        questionSavedDeleteHistory = deleteHistoryRepository.save(new DeleteHistory(2L, ContentType.QUESTION, question.getId(), javajigi));
    }

    @Test
    @DisplayName("delete history 등록")
    public void saveDeleteHistoryTest() {
        Long id = answerSavedDeleteHistory.getId();
        DeleteHistory deleteHistory = deleteHistoryRepository.findById(id).get();
        assertAll(
                () -> assertNotNull(answerSavedDeleteHistory.getId()),
                () -> assertEquals(deleteHistory, answerSavedDeleteHistory)
        );
    }

    @Test
    @DisplayName("content type이 answer인 삭제 이력 조회")
    public void findByContentTypeIsAnswerTest() {
        Long answerId = answerSavedDeleteHistory.getId();
        Long questionId = questionSavedDeleteHistory.getId();
        Optional<DeleteHistory> oAnswerHistory = deleteHistoryRepository.findById(answerId);
        Optional<DeleteHistory> oQuestionHistory = deleteHistoryRepository.findById(questionId);
        List<DeleteHistory> deleteHistories = deleteHistoryRepository.findByContentType(ContentType.ANSWER);
        assertAll(
                () -> assertThat(deleteHistories).contains(oAnswerHistory.get()),
                () -> assertThat(deleteHistories).doesNotContain(oQuestionHistory.get())
        );
    }

    @Test
    @DisplayName("content type이 question인 삭제 이력 조회")
    public void findByContentTypeIsQuestionTest() {
        Long answerId = answerSavedDeleteHistory.getId();
        Long questionId = questionSavedDeleteHistory.getId();
        Optional<DeleteHistory> oAnswerHistory = deleteHistoryRepository.findById(answerId);
        Optional<DeleteHistory> oQuestionHistory = deleteHistoryRepository.findById(questionId);
        List<DeleteHistory> deleteHistories = deleteHistoryRepository.findByContentType(ContentType.QUESTION);
        assertAll(
                () -> assertThat(deleteHistories).contains(oQuestionHistory.get()),
                () -> assertThat(deleteHistories).doesNotContain(oAnswerHistory.get())
        );
    }
}
