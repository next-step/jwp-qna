package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DeleteHistoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    private DeleteHistory DH1;

    @BeforeEach
    void init() {
        DH1 = new DeleteHistory(
            ContentType.QUESTION,
            1L,
            1L,
            LocalDateTime.of(2021, 6, 1, 0, 0, 0)
        );
        deleteHistoryRepository.save(DH1);
    }

    @Test
    @DisplayName(value = "저장된 row 를 select 해온다")
    void select() {
        List<DeleteHistory> selectAll = deleteHistoryRepository.findAll();
        assertThat(selectAll.size()).isEqualTo(1);
    }

    @Test
    @DisplayName(value = "DB에 저장한 객체와 저장전 객체는 동일하다")
    void insert() {
        DeleteHistory actual = new DeleteHistory(
            ContentType.QUESTION,
            1L,
            1L,
            LocalDateTime.of(2021, 6, 1, 0, 0, 0)
        );
        DeleteHistory saved = deleteHistoryRepository.save(actual);
        assertThat(saved).isEqualTo(actual);
    }

}