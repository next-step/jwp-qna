package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EnableJpaAuditing
@DisplayName("DeleteHistory 테스트")
class DeleteHistoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Test
    @DisplayName("DeleteHistory를 저장한다.")
    void save() {
        // given
        User user = new User("test_id", "Passw0rd!", "홍길동", "test@email.com");
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, user);

        // when
        DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(deleteHistory);

        // then
        assertAll(
                () -> assertThat(savedDeleteHistory.getId()).isNotNull(),
                () -> assertThat(savedDeleteHistory.getCreateDate()).isNotNull(),
                () -> assertThat(savedDeleteHistory).isEqualTo(deleteHistory)
        );
    }
}
