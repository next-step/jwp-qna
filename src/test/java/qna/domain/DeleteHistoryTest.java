package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class DeleteHistoryTest {
    private static final DeleteHistory DH_ANSWER =  new DeleteHistory(1L, ContentType.ANSWER, 1L, 1L);
    private static final DeleteHistory DH_QUESTION =  new DeleteHistory(2L, ContentType.QUESTION, 2L, 2L);

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private EntityManager entityManager;

    private DeleteHistory answerSavedDeleteHistory;
    private DeleteHistory questionDeleteHistory;

    @BeforeEach
    private void beforeEach(){
        answerSavedDeleteHistory = deleteHistoryRepository.save(DH_ANSWER);
        questionDeleteHistory = deleteHistoryRepository.save(DH_QUESTION);
    }

    @AfterEach
    private void afterEach(){
        entityManager
                .createNativeQuery("ALTER TABLE delete_history ALTER COLUMN `id` RESTART WITH 1")
                .executeUpdate();
    }

    @Test
    @DisplayName("delete history 등록")
    public void saveDeleteHistoryTest(){
        assertThat(answerSavedDeleteHistory).isEqualTo(DH_ANSWER);
        assertThat(questionDeleteHistory).isEqualTo(DH_QUESTION);
    }

    @Test
    @DisplayName("content type이 answer인 삭제 이력 조회")
    public void findByContentTypeIsAnswerTest(){
        List<DeleteHistory> deleteHistories = deleteHistoryRepository.findByContentType(ContentType.ANSWER);

        assertAll(
                () -> assertThat(deleteHistories).contains(DH_ANSWER),
                () -> assertThat(deleteHistories).doesNotContain(DH_QUESTION)
        );
    }

    @Test
    @DisplayName("content type이 question인 삭제 이력 조회")
    public void findByContentTypeIsQuestionTest(){
        List<DeleteHistory> deleteHistories = deleteHistoryRepository.findByContentType(ContentType.QUESTION);

        assertAll(
                () -> assertThat(deleteHistories).contains(DH_QUESTION),
                () -> assertThat(deleteHistories).doesNotContain(DH_ANSWER)
        );
    }
}
