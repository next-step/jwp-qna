package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistories;

    private User user1;

    @BeforeEach
    void setUp() {
        user1 = new User("userId1", "1234", "userName1", "userEmail1");
    }

    @DisplayName("삭제 이력을 저장한다.")
    @Test
    void save() {
        //given
        DeleteHistory deleteHistory = new DeleteHistory(
                ContentType.ANSWER, 1L, user1, LocalDateTime.now());

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
                ContentType.QUESTION, 1L, user1, LocalDateTime.now()));

        //when
        DeleteHistory actual = deleteHistories.findById(deleteHistory.getId())
                .orElseThrow(NotFoundException::new);

        //then
        assertThat(actual).isSameAs(deleteHistory);
    }

    @DisplayName("삭제 이력을 삭제 아이디로 조회했을 때 엔티티가 없다면 NotFoundException 을 발생시킨다.")
    @Test
    void findByIdException() {
        //given
        //when
        assertThatThrownBy(() -> deleteHistories.findById(-1L)
                .orElseThrow(NotFoundException::new))
                .isInstanceOf(NotFoundException.class); //then
    }
}