package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import qna.domain.utils.JpaTest;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteHistoryRepositoryTest {
    private User user1 = new User("tester", "password", "tester", "test@test.com");

    private DeleteHistory deleteHistory1 = DeleteHistory.ofQuestion(1L, user1);

    @Nested
    @DisplayName("save 메서드는")
    class Describe_save {

        @Nested
        @DisplayName("삭제할 이력 정보가 주어지면")
        class Context_with_data extends JpaTest {
            final DeleteHistory deleteHistory = deleteHistory1;

            @Test
            @DisplayName("삭제 이력을 저장하고, 삭제 이력 객체를 리턴한다")
            void it_saves_delete_history_and_returns_delete_history() {
                DeleteHistory actual = getDeleteHistoryRepository().save(deleteHistory);

                assertThat(actual).isEqualTo(deleteHistory);
            }
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class Describe_find_by_id {

        @Nested
        @DisplayName("저장된 삭제 이력과 식별자가 주어지면 ")
        class Context_with_deleted_history_and_id extends JpaTest {
            DeleteHistory givenDeleteHistory;
            long givenId() {
                return givenDeleteHistory.getId();
            }

            @BeforeEach
            void setUp() {
                givenDeleteHistory = getDeleteHistoryRepository().save(deleteHistory1);
            }

            @Test
            @DisplayName("삭제정보 식별키에 해당하는 삭제 정보를 리턴한다")
            void it_returns_user() {
                DeleteHistory actual = getDeleteHistoryRepository().findById(givenId())
                        .orElseThrow(EntityNotFoundException::new);

                assertThat(actual).isEqualTo(deleteHistory1);
            }
        }
    }
}
