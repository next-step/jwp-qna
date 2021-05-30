package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistories;
    
    @Test
    @DisplayName("삭제이력 저장")
    void save() {
        // given
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, QuestionTest.Q1.getId(), UserTest.JAVAJIGI, LocalDateTime.now());

        // when
        DeleteHistory actual = deleteHistories.save(deleteHistory);

        // then
        assertThat(actual).isSameAs(deleteHistory);
    }
}