package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class DeleteHistoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void cleanUp() {
        deleteHistoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("deleteHistory 저장 테스트")
    @Test
    void deleteHistorySaveTest() {
        //given
        LocalDateTime now =  LocalDateTime.now();
        User deletedByUser = new User("id", "password", " name", "email");
        userRepository.save(deletedByUser);
        DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(new DeleteHistory(ContentType.QUESTION, 1L, deletedByUser, LocalDateTime.now()));

        //when
        List<DeleteHistory> deleteHistorys = deleteHistoryRepository.findAll();

        //then
        DeleteHistory deleteHistory = deleteHistorys.get(0);
        assertAll(() -> {
            assertThat(deleteHistory.getId(), is(notNullValue()));
            assertThat(deleteHistory.getContentType(), is(ContentType.QUESTION));
            assertThat(deleteHistory.getContentId(), is(savedDeleteHistory.getContentId()));
            assertThat(deleteHistory.getDeletedByUser(), is(savedDeleteHistory.getDeletedByUser()));
            assertTrue(deleteHistory.getCreateDate().isAfter(now));
        });
    }
}
