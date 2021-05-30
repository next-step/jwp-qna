package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

@DataJpaTest
public class DeleteHistoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        DeleteHistory expectedDeleteHistory = new DeleteHistory(ContentType.ANSWER, AnswerTest.ANSWER1.getId(), AnswerTest.ANSWER1.getWriterId(), LocalDateTime.now());
        DeleteHistory deleteHistory = deleteHistoryRepository.save(expectedDeleteHistory);

        DeleteHistory expectedDeleteHistory2 = new DeleteHistory(ContentType.QUESTION, QuestionTest.QUESTION1.getId(), QuestionTest.QUESTION1.getWriterId(), LocalDateTime.now());
        DeleteHistory deleteHistory2 = deleteHistoryRepository.save(expectedDeleteHistory2);
    }
}
