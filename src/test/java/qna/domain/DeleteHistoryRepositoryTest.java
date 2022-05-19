package qna.domain;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    DeleteHistory d1 = new DeleteHistory(ContentType.ANSWER, 0L, 1L,
            LocalDateTime.of(2022, Month.APRIL, 1, 10, 10));

    DeleteHistory d2 = new DeleteHistory(ContentType.QUESTION, 0L, 1L,
            LocalDateTime.of(2021, Month.APRIL, 1, 10, 10));


    @Test
    @DisplayName("삭제이력저장")
    void save() {
        DeleteHistory save = deleteHistoryRepository.save(d1);
        assertAll(()-> {
            assertThat(deleteHistoryRepository.findById(save.getId()).isPresent()).isTrue();
            assertThat(deleteHistoryRepository.findById(save.getId()).get()).isEqualTo(save);
        });
    }

    @Test
    @DisplayName("전체 삭제이력을 조회 한다.")
    void findAll() {
        deleteHistoryRepository.saveAll(Arrays.asList(d1, d2));
        List<DeleteHistory> deleteHistories = deleteHistoryRepository.findAll();
        assertThat(deleteHistories).containsAll(Arrays.asList(d1, d2));
    }

    @Test
    @DisplayName("컨텐츠 타입별 조회")
    void findByContentType() {
        deleteHistoryRepository.saveAll(Arrays.asList(d1, d2));
        assertThat(deleteHistoryRepository.findByContentType(ContentType.ANSWER))
                .contains(d1);
    }

    @Test
    @DisplayName("삭제이력삭제")
    void delete() {
        DeleteHistory d1History = deleteHistoryRepository.save(d1);
        DeleteHistory d2History  = deleteHistoryRepository.save(d2);

        deleteHistoryRepository.delete(d1);
        assertAll(() -> {
            assertThat(deleteHistoryRepository.count()).isEqualTo(1);
            assertThat(deleteHistoryRepository.findById(d1History.getId()).isPresent()).isFalse();
        });
    }


    @Test
    @DisplayName("삭제이력변경")
    void update() {
        DeleteHistory save = deleteHistoryRepository.save(d1);
        save.changeContentType(ContentType.QUESTION);

        assertThat(deleteHistoryRepository.findById(save.getId()).get().getContentType())
                .isEqualTo(ContentType.QUESTION);
    }




}