package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DeleteHistoryRepositoryTest extends JpaTest {
    private User user1;

    @BeforeEach
    void setUp() {
        user1 = new User("userId1", "1234", "userName1", "userEmail1");
        getUsers().save(user1);
    }

    @DisplayName("삭제 이력을 저장한다.")
    @Test
    void save() {
        //given
        DeleteHistory deleteHistory = new DeleteHistory(
                ContentType.ANSWER, 1L, user1, LocalDateTime.now());

        //when
        DeleteHistory actual = getDeleteHistories().save(deleteHistory);

        //then
        assertThat(actual).isSameAs(deleteHistory);
    }

    @DisplayName("삭제 이력을 삭제 아이디로 조회한다.")
    @Test
    void findById() {
        //given
        DeleteHistory deleteHistory = getDeleteHistories().save(new DeleteHistory(
                ContentType.QUESTION, 1L, user1, LocalDateTime.now()));

        //when
        DeleteHistory actual = getDeleteHistories().findById(deleteHistory.getId())
                .orElseThrow(EntityNotFoundException::new);

        //then
        assertThat(actual).isSameAs(deleteHistory);
    }

    @DisplayName("삭제 이력을 삭제 아이디로 조회했을 때 엔티티가 없다면 EntityNotFoundException 을 발생시킨다.")
    @Test
    void findByIdException() {
        //given
        //when
        assertThatThrownBy(() -> getDeleteHistories().findById(-1L)
                .orElseThrow(EntityNotFoundException::new))
                .isInstanceOf(EntityNotFoundException.class); //then
    }
}