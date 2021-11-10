package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName : qna.domain
 * fileName : DeleteHistoryRepositoryTest
 * author : haedoang
 * date : 2021-11-09
 * description :
 */
@DataJpaTest
@EnableJpaAuditing
@TestMethodOrder(MethodOrderer.MethodName.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository repository;

    @Test
    @DisplayName("DeleteHistory save")
    public void T1_save() {
        //GIVEN
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());
        //WHEN
        DeleteHistory savedHistory = repository.save(deleteHistory);
        //THEN
        assertThat(savedHistory.equals(deleteHistory)).isTrue();
    }

}
