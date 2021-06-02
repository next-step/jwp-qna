package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    DeleteHistoryRepository repository;

    private DeleteHistory deleteHistory;

    private DeleteHistory actual;

    @BeforeEach
    void setUp() {
        User deletedUser = User.of("tj", "ps", "김석진", "7271kim@naver.com");
        deleteHistory = DeleteHistory.of(ContentType.ANSWER, 1L, deletedUser);
        actual = repository.save(deleteHistory);
    }

    @Test
    @DisplayName("정상적으로 전 후 데이터가 들어가 있는지 확인한다.")
    void save() {
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getCreateDate()).isNotNull(),
            () -> assertThat(actual.getContentId()).isEqualTo(deleteHistory.getContentId()),
            () -> assertThat(actual.getContentType()).isEqualTo(deleteHistory.getContentType()),
            () -> assertThat(actual.getDeletedBy()).isEqualTo(deleteHistory.getDeletedBy()));
    }

    @Test
    @DisplayName("update 확인")
    void updata() {
        deleteHistory.setContentType(ContentType.QUESTION);
        repository.save(deleteHistory);
        DeleteHistory finedDeleteHistory = repository.findById(deleteHistory.getId()).get();
        assertThat(finedDeleteHistory.getContentType()).isEqualTo(ContentType.QUESTION);
    }
}
