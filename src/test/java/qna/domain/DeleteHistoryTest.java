package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EnableJpaAuditing
class DeleteHistoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;


    @DisplayName("삭제내역을 저장한다.")
    @Test
    void save() {
        // given
        User user = new User("id1234", "password", "name", "mail@email.com");
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, user);

        // when
        DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(deleteHistory);

        // then
        assertAll(
                () -> assertThat(savedDeleteHistory.getId()).isNotNull(),
                () -> assertThat(savedDeleteHistory).isEqualTo(deleteHistory)
        );
    }

}
