package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.QuestionEntityTest.provideQuestion;

@DataJpaTest
@DisplayName("delete_history 엔티티 테스트")
public class DeleteHistoryRepositoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    UserRepository userRepository;

    @DisplayName("save 성공")
    @Test
    void save_deleteHistory_success() {
        //given:
        final User user = userRepository.save(UserRepositoryTest.MINGVEL);
        final Question question = provideQuestion().writeBy(user);
        //when:
        final DeleteHistory deleteHistory = deleteHistoryRepository.save(DeleteHistory.fromQuestion(question));
        //then:
        assertThat(deleteHistory.getDeletedByUser()).isEqualTo(user);
    }
}
