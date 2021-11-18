package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void save() {
        User savedUser = userRepository.save(TestDummy.USER_SANJIGI);
        final DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, savedUser);

        final DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(deleteHistory);

        assertThat(savedDeleteHistory).isEqualTo(deleteHistory);
    }
}
