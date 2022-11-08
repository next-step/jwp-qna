package qna.domain;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static qna.fixture.TestFixture.JAVAJIGI;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    private User user;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = userRepository.save(JAVAJIGI);
    }

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @DisplayName("save 검증 성공")
    @Test
    void saveTest() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, user, LocalDateTime.now());

        DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(deleteHistory);

        assertNotNull(savedDeleteHistory.getId());
    }
}
