package qna.domain;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.ContentType.ANSWER;
import static qna.domain.ContentType.QUESTION;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    private final static DeleteHistory d1 = new DeleteHistory(ANSWER, 0L, JAVAJIGI,
            LocalDateTime.of(2022, Month.APRIL, 1, 10, 10));

    private final static DeleteHistory d2 = new DeleteHistory(QUESTION, 0L, SANJIGI,
            LocalDateTime.of(2021, Month.APRIL, 1, 10, 10));

    @BeforeEach
    void setUp() {
        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);
    }


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
        final List<DeleteHistory> deleteHistories = deleteHistoryRepository.saveAll(Arrays.asList(d1, d2));

        List<DeleteHistory> findHistory = deleteHistoryRepository.findAll();

        assertThat(deleteHistories).containsAll(findHistory);
    }

    @Test
    @DisplayName("컨텐츠 타입별 조회")
    void findByContentType() {
        final List<DeleteHistory> deleteHistories = deleteHistoryRepository.saveAll(Arrays.asList(d1, d2));

        assertThat(deleteHistoryRepository.findByContentType(ANSWER))
                .hasSize(1)
                .contains(deleteHistories.get(0));
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
        save.changeContentType(QUESTION);

        assertThat(deleteHistoryRepository.findById(save.getId()).get().getContentType())
                .isEqualTo(QUESTION);
    }




}