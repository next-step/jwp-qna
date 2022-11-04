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
    private DeleteHistoryRepository repository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("test1234", "1234", "테스트", "test1234@gmail.com"));
    }

    @DisplayName("삭제 내역을 저장한다.")
    @Test
    void save() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, user, LocalDateTime.now());
        final DeleteHistory savedDeleteHistory = repository.save(deleteHistory);
        assertThat(savedDeleteHistory).isEqualTo(deleteHistory);
    }
}
