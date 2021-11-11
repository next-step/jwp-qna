package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.DeleteHistoryTest.DH_ANSWER;
import static qna.domain.DeleteHistoryTest.DH_QUESTION;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    private DeleteHistory answerSavedDeleteHistory;
    private DeleteHistory questionSavedDeleteHistory;

    @BeforeEach
    private void beforeEach() {
        answerSavedDeleteHistory = deleteHistoryRepository.save(DH_ANSWER);
        questionSavedDeleteHistory = deleteHistoryRepository.save(DH_QUESTION);
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
