package qna.domain;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class DeleteHistoryTest {
    private User user;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("kim9418", "123123", "김대겸", "koreatech93@naver.com"));
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
