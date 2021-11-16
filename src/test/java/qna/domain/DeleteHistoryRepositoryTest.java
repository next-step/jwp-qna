package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("delete history 저장 성공")
    public void saveDeleteHistorySuccess() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, javajigi);

        DeleteHistory save = deleteHistoryRepository.save(deleteHistory);

        assertThat(save.equals(deleteHistory)).isTrue();
    }
}