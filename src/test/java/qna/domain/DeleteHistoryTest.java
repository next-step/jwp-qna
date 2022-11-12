package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaAuditing
class DeleteHistoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistories;

    @Autowired
    UserRepository users;

    DeleteHistory history;
    User user;

    @BeforeEach
    void setUp() {
        user = users.save(new User("userId", "password", "name", "email"));
        history = deleteHistories.save(new DeleteHistory(ContentType.QUESTION, 1L, user, LocalDateTime.now()));
    }

    @Test
    void save_테스트() {
        assertThat(history.getId()).isNotNull();
        assertThat(history.getCreateDate()).isNotNull();
    }

    @Test
    void save_후_findById_테스트() {
        DeleteHistory findHistory = deleteHistories.findById(history.getId()).get();
        assertThat(history).isEqualTo(findHistory);
    }
}