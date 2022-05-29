package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    UserRepository userRepository;

    User testUser;

    @BeforeEach
    void before() {
        testUser = new User("test", "pw", "테스트유저", "a@naver.com");
        userRepository.save(testUser);
    }

    @DisplayName("DeleteHistory 저장 테스트")
    @Test
    void saveTest() {
        DeleteHistory expected = new DeleteHistory(ContentType.ANSWER, 1L, testUser, LocalDateTime.now());
        DeleteHistory result = deleteHistoryRepository.save(expected);

        assertThat(result).isNotNull();
        assertThat(result.getContentType()).isEqualTo(expected.getContentType());
    }

    @DisplayName("DeleteHistory 조회 테스트")
    @Test
    void findTest() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, testUser, LocalDateTime.now());
        DeleteHistory expected = deleteHistoryRepository.save(deleteHistory);
        Optional<DeleteHistory> resultOptional = deleteHistoryRepository.findById(expected.getId());
        assertThat(resultOptional).isNotEmpty();

        DeleteHistory result = resultOptional.get();
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected);
    }
}