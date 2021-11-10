package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeleteHistoryTest {
    public static final DeleteHistory DH1 = new DeleteHistory(ContentType.QUESTION, QuestionTest.Q1.getId(), UserTest.LEWISSEO.getId(), LocalDateTime.now());
    public static final DeleteHistory DH2 = new DeleteHistory(ContentType.QUESTION, AnswerTest.A1.getId(), UserTest.JAVAJIGI.getId(), LocalDateTime.now());

    @Autowired
    private DeleteHistoryRepository histories;
    private DeleteHistory deleteHistory;

    @BeforeEach
    void setUp() {
        deleteHistory = histories.save(DH1);
    }

    @DisplayName("deleteHistory 생성")
    @Test
    void saveDeleteHistoryTest() {
        assertThat(histories.findAll()).isNotNull();
    }

    @DisplayName("deleteHistory 삭제")
    @Test
    void removeDeleteHistoryTest() {
        assertThat(histories.findAll().size()).isEqualTo(1);
        histories.delete(deleteHistory);
        assertThat(histories.findAll().size()).isZero();
    }
}
