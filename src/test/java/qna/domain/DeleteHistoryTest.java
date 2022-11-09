package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("delete_history 엔티티 테스트")
public class DeleteHistoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    UserRepository userRepository;

    @DisplayName("save 성공")
    @Test
    void save_deleteHistory_success() {
        //given:
        final User user = userRepository.save(UserTest.MINGVEL);
        //when:
        DeleteHistory deleteHistory = deleteHistoryRepository.save(new DeleteHistory(ContentType.QUESTION, 1L, user));
        //then:
        assertThat(deleteHistory.getDeletedByUser()).isEqualTo(user);
    }
}
