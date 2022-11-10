package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.DeleteHistoryRepository;
import qna.domain.QuestionRepository;
import qna.domain.User;
import qna.domain.UserRepository;
import qna.domain.UserTest;

@DisplayName("DeleteHistory_관련_테스트")
@DataJpaTest
public class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestEntityManager manager;

    @AfterEach
    void after() {
        manager.flush();
        manager.clear();
    }

    @DisplayName("저장_확인")
    @Test
    void save() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        DeleteHistory dummyData = new DeleteHistory(ContentType.QUESTION, 1L, user, LocalDateTime.now());
        DeleteHistory expectedResult = deleteHistoryRepository.save(dummyData);
        assertAll(
                () -> assertThat(expectedResult.getId()).isNotNull(),
                () -> assertThat(expectedResult.getContentId()).isEqualTo(dummyData.getContentId()),
                () -> assertThat(expectedResult.getDeletedBy()).isEqualTo(dummyData.getDeletedBy())
        );
    }
}
