package qna.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    DeleteHistoryRepository repository;

    @Autowired
    TestEntityManager em;

    @Test
    @DisplayName("@Enumerated 애너테이션으로 ContentType 을 String 값으로 저장한다")
    void 엔티티_저장() {
        DeleteHistory expected = new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());
        DeleteHistory actual = repository.save(expected);
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual == expected).isTrue();
    }

    @Test
    @DisplayName("준영속 엔티티 비교시 equals() hashCode() 활용")
    void equals_hashCode() {
        DeleteHistory expected = repository.save(new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now()));
        flushAndClear();
        DeleteHistory actual = repository.findById(expected.getId()).get();
        assertThat(actual).isEqualTo(expected);
        assertThat(actual == expected).isFalse();
    }

    @Test
    void Id로_삭제() {
        DeleteHistory actual = repository.save(new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now()));
        repository.delete(actual);
        repository.flush();
    }

    private void flushAndClear() {
        em.flush();
        em.clear();
    }
}
