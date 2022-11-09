package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest
public class DeleteHistoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Test
    void save() {
        final User JAVAJIGI = new User("javajigi", "password", "name", "javajigi@slipp.net");
        final User SANJIGI = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
        final Question Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
        final Question Q2 = new Question("title2", "contents2").writeBy(SANJIGI);
        final Answer A1 = new Answer(JAVAJIGI, Q1, "Answers Contents1");
        final Answer A2 = new Answer(SANJIGI, Q2, "Answers Contents2");
        final DeleteHistory deleteHistory1 = new DeleteHistory(ContentType.ANSWER, A1.getId(), JAVAJIGI,
                LocalDateTime.now());
        final DeleteHistory deleteHistory2 = new DeleteHistory(ContentType.QUESTION, Q2.getId(), JAVAJIGI,
                LocalDateTime.now());
        final DeleteHistory deleteHistory3 = new DeleteHistory(ContentType.ANSWER, A2.getId(), JAVAJIGI,
                LocalDateTime.now());
        assertAll(
                () -> assertDoesNotThrow(() -> deleteHistoryRepository.save(deleteHistory1)),
                () -> assertDoesNotThrow(() -> deleteHistoryRepository.save(deleteHistory2)),
                () -> assertDoesNotThrow(() -> deleteHistoryRepository.save(deleteHistory3))
        );
    }
}
