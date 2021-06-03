package qna.domain.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.entity.DeleteHistory;
import qna.domain.entity.User;
import qna.domain.entity.UserTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = userRepository.save(UserTest.USER_JAVAJIGI);
    }

    @Test
    public void exists() {
        DeleteHistory deleteHistory = DeleteHistory.builder()
                .user(user)
                .contentType(DeleteHistory.ContentType.ANSWER)
                .contentId(1l)
                .createDate(LocalDateTime.now())
                .build();

        DeleteHistory actual = deleteHistoryRepository.save(deleteHistory);

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.getContentType()).isEqualTo(DeleteHistory.ContentType.ANSWER)
        );
    }
}
