package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    private DeleteHistory savedDeleteHistory;

    @BeforeEach
    void setUp() {
        User deletedBy = userRepository.save(UserTest.JAVAJIGI);
        this.savedDeleteHistory = deleteHistoryRepository.save(
                new DeleteHistory(ContentType.QUESTION, 1L, deletedBy, LocalDateTime.now()));
    }

    @DisplayName("save하면 pk가 존재한다")
    @Test
    void save() {
        assertThat(savedDeleteHistory.getId()).isNotNull();
    }

    @DisplayName("동일성 확인")
    @Test
    void findById() {
        // given & when
        DeleteHistory actual = deleteHistoryRepository.findById(savedDeleteHistory.getId()).get();

        // then
        assertThat(savedDeleteHistory).isEqualTo(actual);
    }
}
