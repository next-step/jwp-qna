package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
class DeleteHistoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(UserTest.JAVAJIGI);
    }

    @Test
    public void 삭제이력_저장() {
        //given
        DeleteHistory actual = deleteHistoryRepository.save(new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI, LocalDateTime.now()));
        Long savedId = actual.getId();

        //when
        DeleteHistory expected = deleteHistoryRepository.findById(savedId).get();

        //then
        assertThat(actual).isSameAs(expected);
    }

    @Test
    public void 사용자_삭제이력_조회() {
        //given
        deleteHistoryRepository.save(new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI, LocalDateTime.now()));
        deleteHistoryRepository.save(new DeleteHistory(ContentType.ANSWER, 2L, UserTest.JAVAJIGI, LocalDateTime.now()));

        //when
        List<DeleteHistory> expected = deleteHistoryRepository.findByDeletedById(1L);

        //then
        assertThat(expected).hasSize(2);
    }

}