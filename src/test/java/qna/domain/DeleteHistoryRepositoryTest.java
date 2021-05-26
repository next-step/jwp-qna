package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository repository;

    DeleteHistory deleteHistory;
    DeleteHistory savedDeleteHistory;

    @BeforeEach
    void setUp() {
        deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, 1L);
        savedDeleteHistory = repository.save(deleteHistory);
    }

    @Test
    @DisplayName("서로 같은 데이터를 가진 엔티티는 동일해야 한다")
    void entitySameAsTest() {
        assertThat(savedDeleteHistory).isSameAs(deleteHistory);
    }

    @Test
    @DisplayName("조회한 데이터와 같은 id 값을 가진 엔티티는 동일해야 한다")
    void entityRetrieveTest() {
        DeleteHistory findDeleteHistory = repository.findById(savedDeleteHistory.getId()).get();

        assertThat(findDeleteHistory).isSameAs(savedDeleteHistory);
    }

    @Test
    @DisplayName("저장 전 후의 데이터가 같아야 한다")
    void entitySameValueTest() {
        assertAll(
                () -> assertThat(savedDeleteHistory.getId()).isNotNull(),
                () -> assertThat(savedDeleteHistory.getContentType()).isEqualTo(deleteHistory.getContentType()),
                () -> assertThat(savedDeleteHistory.getContentId()).isEqualTo(deleteHistory.getContentId()),
                () -> assertThat(savedDeleteHistory.getDeletedById()).isEqualTo(deleteHistory.getDeletedById())
        );

    }

    @Test
    @DisplayName("inert 시 createAt 이 자동으로 입력된다.")
    void dateAutoCreateTest() {

        assertAll(
                () -> assertThat(savedDeleteHistory.getCreateAt()).isNotNull(),
                () -> assertThat(savedDeleteHistory.getUpdateAt()).isNull()
        );
    }

    @Test
    @DisplayName("update 시 updateAt 이 자동으로 변경된다.")
    void dateAutoModifyTest() {
        savedDeleteHistory.setContentId(3L);
        repository.flush();

        assertThat(savedDeleteHistory.getUpdateAt()).isNotNull();
    }

    @AfterEach
    void endUp() {
        repository.deleteAll();
    }
}