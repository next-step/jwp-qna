package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DeleteHistoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName(value = "저장된 row 를 select 해온다")
    void select() {
        insertHistory();
        List<DeleteHistory> selectAll = deleteHistoryRepository.findAll();
        assertThat(selectAll.size()).isEqualTo(1);
    }

    private DeleteHistory insertHistory() {
        User user = userRepository.saveAndFlush(UserTest.JAVAJIGI);
        DeleteHistory deleteHistory = new DeleteHistory(
            ContentType.QUESTION,
            1L,
            user,
            LocalDateTime.of(2021, 6, 1, 0, 0, 0)
        );
        return deleteHistoryRepository.saveAndFlush(deleteHistory);
    }

}