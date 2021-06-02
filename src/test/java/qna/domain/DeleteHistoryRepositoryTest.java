package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DeleteHistoryRepositoryTest extends JpaTest {

    @Autowired
    private DeleteHistoryRepository deleteHistories;

    @DisplayName("삭제 이력을 저장한다.")
    @Test
    void save() {
        //given
        DeleteHistory deleteHistory = new DeleteHistory(
                ContentType.ANSWER, 1L, 1L, LocalDateTime.now());

        //when
        DeleteHistory actual = deleteHistories.save(deleteHistory);

        //then
        assertThat(actual).isSameAs(deleteHistory);
    }

    @DisplayName("삭제 이력을 삭제 아이디로 조회한다.")
    @Test
    void findById() {
        //given
        DeleteHistory deleteHistory = deleteHistories.save(new DeleteHistory(
                ContentType.QUESTION, 1L, 1L, LocalDateTime.now()));

        //when
        DeleteHistory actual = deleteHistories.findById(deleteHistory.getId())
                .orElseThrow(EntityNotFoundException::new);

        //then
        assertThat(actual).isSameAs(deleteHistory);
    }

    @DisplayName("삭제 이력을 삭제 아이디로 조회했을 때 엔티티가 없다면 EntityNotFoundException 을 발생시킨다.")
    @Test
    void findByIdException() {
        //given
        //when
        assertThatThrownBy(() -> deleteHistories.findById(-1L)
                .orElseThrow(EntityNotFoundException::new))
                .isInstanceOf(EntityNotFoundException.class); //then
    }
}