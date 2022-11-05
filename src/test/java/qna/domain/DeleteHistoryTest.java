package qna.domain;

<<<<<<< HEAD
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
=======
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
>>>>>>> 34eef6d (feat: DeleteHistory 엔티티 추가)

@DataJpaTest
public class DeleteHistoryTest {

    private static final DeleteHistory questionDeleted =
        new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
    private static final DeleteHistory answerDeleted =
        new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @DisplayName("save 테스트")
    @Test
    void saveTest() {
        DeleteHistory actual = deleteHistoryRepository.save(questionDeleted);

        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertEquals(ContentType.QUESTION, actual.getContentType()),
            () -> assertEquals(1L, actual.getContentId()),
            () -> assertEquals(1L, actual.getDeletedById()),
            () -> assertThat(actual.getCreateDate()).isNotNull()
        );
    }
}