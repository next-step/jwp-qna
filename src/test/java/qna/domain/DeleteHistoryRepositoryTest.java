package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Test
    @DisplayName("삭제이력 저장 테스트")
    void save(){
        //given
        DeleteHistory saveHistory = deleteHistoryRepository.save(
                new DeleteHistory(ContentType.QUESTION, 10L, 1L, LocalDateTime.now()));

        //when
        DeleteHistory findHistory = deleteHistoryRepository.findById(saveHistory.getId())
                .orElse(null);

        //then
        assertThat(findHistory).isEqualTo(saveHistory);
    }

    @Test
    @DisplayName("모든 삭제된 질문의 내역 조회")
    void findAllByContentTypeTest1(){
        //given
        DeleteHistory saveHistory1 = deleteHistoryRepository.save(
                new DeleteHistory(ContentType.QUESTION, 10L, 1L, LocalDateTime.now()));
        DeleteHistory saveHistory2 = deleteHistoryRepository.save(
                new DeleteHistory(ContentType.QUESTION, 10L, 1L, LocalDateTime.now()));

        //when
        List<DeleteHistory> histories = deleteHistoryRepository.findAllByContentType(ContentType.QUESTION);

        //then
        assertThat(histories).containsExactly(
                saveHistory1, saveHistory2
        );
    }
}
