package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DeleteHistoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    EntityManager em;

    @Test
    public void 삭제이력_저장() {
        //given
        DeleteHistory actual = deleteHistoryRepository.save(new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now()));
        Long savedId = actual.getId();
        em.clear();

        //when
        DeleteHistory expected = deleteHistoryRepository.findById(savedId).get();

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void 사용자_삭제이력_조회() {
        //given
        deleteHistoryRepository.save(new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now()));
        deleteHistoryRepository.save(new DeleteHistory(ContentType.ANSWER, 2L, 1L, LocalDateTime.now()));

        //when
        List<DeleteHistory> expected = deleteHistoryRepository.findByDeletedById(1L);

        //then
        assertThat(2).isEqualTo(expected.size());
    }

}