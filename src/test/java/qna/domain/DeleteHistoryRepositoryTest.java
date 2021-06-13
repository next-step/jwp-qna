package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    private DeleteHistory savedDeleteHistory;
    private User savedUser;

    @BeforeEach
    void setUp() {
        savedUser = userRepository.save(new User( "javajigi", "password", "name", "javajigi@slipp.net"));
        DeleteHistory expect = new DeleteHistory(ContentType.QUESTION, 1L, savedUser, LocalDateTime.now());
        savedDeleteHistory = deleteHistoryRepository.save(expect);
    }

    @DisplayName("저장")
    @Test
    public void saveDeleteHistory() {
        assertThat(savedDeleteHistory).isNotNull();
    }
}
