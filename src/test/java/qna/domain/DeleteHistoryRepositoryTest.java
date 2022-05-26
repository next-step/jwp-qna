package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Test
    public void save() {
        //given
        DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, 1L, UserTest.JAVAJIGI, LocalDateTime.now());
        //when
        DeleteHistory actual = deleteHistoryRepository.save(expected);
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void findById() {
        //given
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, UserTest.JAVAJIGI, LocalDateTime.now());
        DeleteHistory expected = deleteHistoryRepository.save(deleteHistory);
        //when
        DeleteHistory actual = deleteHistoryRepository.findById(1L).get();
        //then
        assertThat(actual).isEqualTo(expected);
    }
}
