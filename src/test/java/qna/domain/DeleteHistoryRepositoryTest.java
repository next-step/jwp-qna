package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistorys;

    @BeforeEach
    void setUp() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, Long.valueOf(1), Long.valueOf(1));

        deleteHistorys.save(deleteHistory);
    }

    @Test
    @DisplayName("delete_history테이블 save 테스트")
    void save() {
        DeleteHistory expected =  new DeleteHistory(ContentType.ANSWER, Long.valueOf(2), Long.valueOf(2));
        DeleteHistory actual = deleteHistorys.save(expected);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("delete_history테이블 select 테스트")
    void findById() {
        DeleteHistory expected = deleteHistorys.findByContentId(Long.valueOf(1)).get();

        assertThat(expected).isNotNull();
    }

    @Test
    @DisplayName("delete_history테이블 update 테스트")
    void updateDeletedById() {
        DeleteHistory expected = deleteHistorys.findByContentId(Long.valueOf(1))
                .get();
        expected.setDeletedById(Long.valueOf(2));
        DeleteHistory actual = deleteHistorys.findByContentId(Long.valueOf(1))
                .get();

        assertThat(actual.getDeletedById()).isEqualTo(Long.valueOf(2));
    }

    @Test
    @DisplayName("delete_history테이블 delete 테스트")
    void delete() {
        DeleteHistory expected = deleteHistorys.findByContentId(Long.valueOf(1))
                .get();
        deleteHistorys.delete(expected);

        assertThat(deleteHistorys.findByContentId(Long.valueOf(1)).isPresent()).isFalse();
    }
}