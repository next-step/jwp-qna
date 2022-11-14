package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;
    private DeleteHistory deleteHistory;

    @BeforeEach
    public void setUp() {
        user = userRepository.save(new User(1L, "javajigi", "password", "name", "javajigi@slipp.net"));
        deleteHistory = deleteHistoryRepository.save(new DeleteHistory(ContentType.ANSWER, 1L, user, LocalDateTime.now()));
    }

    @Test
    public void deleteHistorySave() {
        List<DeleteHistory> deleteHistorys = deleteHistoryRepository.findAll();

        assertAll(
                () -> assertThat(deleteHistorys).isNotNull(),
                () -> assertThat(deleteHistorys).hasSize(1),
                () -> assertThat(deleteHistorys).contains(deleteHistory)
        );
    }

    @Test
    public void deleteHistoryFind() {
        DeleteHistory expectValue = deleteHistoryRepository.findById(1L).orElse(null);

        assertAll(
                () -> assertThat(expectValue).isNotNull(),
                () -> assertThat(expectValue).isEqualTo(deleteHistory)
        );


    }

}