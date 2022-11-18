package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @BeforeEach
    private void setUp() {
        User questionWriter = new User("aaUserId", "aa123123", "aa", "aa@naver.com");
        User answerWriter = new User("bbUserId", "bb123123", "bb", "bb@naver.com");
        deleteHistoryRepository.save(new DeleteHistory(ContentType.ANSWER, 1L, questionWriter, LocalDateTime.now()));
        deleteHistoryRepository.save(new DeleteHistory(ContentType.QUESTION, 1L, answerWriter, LocalDateTime.now()));
    }

    @Test
    @DisplayName("DeleteHistory 저장후 동일한지 확인한다.")
    void whenSave_thenSuccess() {
        User questionWriter = new User("fighting", "djkj123", "fighter", "fighter2@naver.com");
        DeleteHistory expected = deleteHistoryRepository.save( new DeleteHistory(ContentType.QUESTION, 2L, questionWriter, LocalDateTime.now()));

        assertAll(
                () -> assertThat(expected.getId()).isNotNull(),
                () -> assertThat(expected.getWriter()).isEqualTo(questionWriter));

    }
}