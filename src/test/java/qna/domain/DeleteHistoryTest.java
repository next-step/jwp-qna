package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeleteHistoryTest {

    public static final DeleteHistory D1 = new DeleteHistory(ContentType.QUESTION, 1L, UserTest.JAVAJIGI, LocalDateTime.now());
    public static final DeleteHistory D2 = new DeleteHistory(ContentType.ANSWER, 2L, UserTest.SANJIGI, LocalDateTime.now());

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        User sanjigi = userRepository.save(UserTest.SANJIGI);

        D1.setDeletedBy(javajigi);
        D2.setDeletedBy(sanjigi);

        deleteHistoryRepository.save(D1);
        deleteHistoryRepository.save(D2);
    }

    @AfterEach
    void clean() {
        deleteHistoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("주어진 ID에 해당하는 삭제 이력을 리턴한다.")
    void 주어진_ID에_해당하는_삭제_이력을_리턴한다() {
        // given
        DeleteHistory deleteHistory = deleteHistoryRepository.findAll().get(0);

        // when
        DeleteHistory result = deleteHistoryRepository.findById(deleteHistory.getId()).get();

        // then
        assertThat(result).isEqualTo(deleteHistory);
    }

    @Test
    @DisplayName("모든 삭제 이력을 삭제한다.")
    void 모든_삭제_이력을_삭제한다() {
        // given
        List<DeleteHistory> prevResult = deleteHistoryRepository.findAll();
        assertThat(prevResult.size()).isGreaterThan(0);

        // when
        deleteHistoryRepository.deleteAll();

        // then
        List<DeleteHistory> result = deleteHistoryRepository.findAll();
        assertThat(result).isEmpty();
    }
}