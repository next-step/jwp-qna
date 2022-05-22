package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired DeleteHistoryRepository deleteHistoryRepository;
    @Autowired UserRepository userRepository;

    private User user;

    @BeforeEach
    void initialize(){
        user = userRepository.save(UserTest.JAVAJIGI);
    }

    @Test
    @DisplayName("DeleteHistory 저장")
    void save(){
        DeleteHistory history = new DeleteHistory(ContentType.QUESTION, 1L, user, LocalDateTime.now());
        DeleteHistory saved = deleteHistoryRepository.save(history);
        assertThat(saved.getId()).isNotNull();
    }
}
