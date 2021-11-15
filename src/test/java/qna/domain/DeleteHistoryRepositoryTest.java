package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(UserTest.JAVAJIGI);
    }

    @Test
    void test() {
        DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(new DeleteHistory(ContentType.QUESTION, 1L, UserTest.JAVAJIGI));
        DeleteHistory deleteHistory = deleteHistoryRepository.findById(1L).get();

        assertThat(deleteHistory.getId()).isEqualTo(savedDeleteHistory.getId());
        assertThat(deleteHistory.getContentId()).isEqualTo(savedDeleteHistory.getContentId());
        assertThat(deleteHistory.getDeletedBy()).isEqualTo(savedDeleteHistory.getDeletedBy());
        assertThat(deleteHistory.getCreateDate()).isNotNull();
        assertThat(deleteHistory.getCreateDate()).isEqualTo(savedDeleteHistory.getCreateDate());
    }

}
