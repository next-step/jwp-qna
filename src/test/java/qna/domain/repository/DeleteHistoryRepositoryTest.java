package qna.domain.repository;

import static qna.domain.UserTest.JAVAJIGI;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.DeleteHistory;
import qna.domain.User;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Test
    @DisplayName("DeleteHistory 저장 테스트")
    void createTest() {
        User save = userRepository.save(JAVAJIGI);
        DeleteHistory deleteHistory = DeleteHistory.createByQuestion(1L, save);
        deleteHistoryRepository.save(deleteHistory);
    }
}
