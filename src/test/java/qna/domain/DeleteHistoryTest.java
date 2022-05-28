package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
public class DeleteHistoryTest {
    private DeleteHistory deletedHistory;

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        UserTest.JAVAJIGI.setId(null);
        deletedHistory = new DeleteHistory(ContentType.ANSWER, 1L, userRepository.save(UserTest.JAVAJIGI), LocalDateTime.now());
    }

    @DisplayName("identityTest 테스트")
    @Test
    void identityTest() {
        DeleteHistory deleteHistory = deleteHistoryRepository.save(deletedHistory);
        assertThat(deletedHistory).isSameAs(deleteHistory);
    }

    @DisplayName("검색 테스트")
    @Test
    void findByContentTypeTest() {
        DeleteHistory deleteHistory = deleteHistoryRepository.save(deletedHistory);
        Optional<DeleteHistory> isDeleteHistory = deleteHistoryRepository.findById(deletedHistory.getId());
        assertThat(isDeleteHistory.isPresent()).isTrue();
        assertThat(isDeleteHistory.get()).isSameAs(deleteHistory);
    }

    @DisplayName("인자 값에 의해 삭제되었는지 확인")
    @Test
    void isDeletedByTest() {
        Predicate<DeleteHistory> predicate = deleteHistory -> {
            return Objects.equals(deleteHistory.getDeletedByUser(), UserTest.JAVAJIGI) &&
                    Objects.equals(deleteHistory.getContentId(), 1L) &&
                    Objects.equals(deleteHistory.getContentType(), ContentType.ANSWER);
        };

        assertThat(deletedHistory.isDeletedBy(predicate)).isTrue();
    }

    @DisplayName("isDeltedBy 메소드의 인자값은 null 이 될수 없다.")
    @Test
    void invalidIsDeletedByTest() {
        assertThatThrownBy(() -> deletedHistory.isDeletedBy(null))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
