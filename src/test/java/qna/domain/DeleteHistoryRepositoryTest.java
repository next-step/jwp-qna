package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistories;
    @Autowired
    private UserRepository users;
    private User userTryingDelete;

    @BeforeEach
    void setUp() {
        userTryingDelete = users.save(UserTest.SANJIGI);
    }

    @AfterEach
    void tearDown() {
        deleteHistories.deleteAll();
    }

    @Test
    void SaveTest(){

        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 11L, userTryingDelete, LocalDateTime.now());
        DeleteHistory actual = deleteHistories.save(deleteHistory);
        assertThat(actual.getId()).isNotNull();
    }
}