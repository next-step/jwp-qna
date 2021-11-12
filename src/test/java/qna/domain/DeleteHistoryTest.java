package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@EnableJpaAuditing
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class DeleteHistoryTest {
    public static final DeleteHistory DH1 = new DeleteHistory(ContentType.QUESTION, QuestionTest.Q1.getId(), UserTest.LEWISSEO.getId(), LocalDateTime.now());
    public static final DeleteHistory DH2 = new DeleteHistory(ContentType.QUESTION, AnswerTest.A1.getId(), UserTest.JAVAJIGI.getId(), LocalDateTime.now());

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;
    private DeleteHistory deleteHistory;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        deleteHistory = deleteHistoryRepository.save(DH1);
    }

    @DisplayName("deleteHistory 생성")
    @Test
    void saveDeleteHistoryTest() {
        assertAll(
                () -> assertThat(deleteHistory.getId()).isNotNull(),
                () -> assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.QUESTION),
                () -> assertThat(deleteHistory.getContentId()).isEqualTo(QuestionTest.Q1.getId()),
                () -> assertThat(deleteHistory.getDeletedById()).isEqualTo(UserTest.LEWISSEO.getId()),
                () -> assertThat(deleteHistory.getCreateDate()).isBefore(now)
        );
    }

    @DisplayName("deleteHistory 삭제")
    @Test
    void removeDeleteHistoryTest() {
        assertThat(deleteHistoryRepository.findAll().size()).isEqualTo(1);
        deleteHistoryRepository.delete(deleteHistory);
        assertThat(deleteHistoryRepository.findAll().size()).isZero();
    }
}
