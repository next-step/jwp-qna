package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryTest {
    public static final DeleteHistory D1 = new DeleteHistory(ContentType.QUESTION, UserTest.JAVAJIGI.getId(), UserTest.SANJIGI,
        LocalDateTime.now());

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void setup() {
        UserTest.JAVAJIGI.setId(null);
        final User user = userRepository.save(UserTest.JAVAJIGI);
        D1.setDeletedBy(user);
    }


    @Test
    @DisplayName("DeleteHistory Entity Create 및 ID 생성 테스트")
    void save() {
        final DeleteHistory deleteHistory = deleteHistoryRepository.save(D1);
        assertThat(deleteHistory.getId()).isNotNull();
    }

    @Test
    @DisplayName("DeleteHistory Entity Read 테스트")
    void findById() {
        final DeleteHistory saved = deleteHistoryRepository.save(D1);
        final DeleteHistory found = deleteHistoryRepository.findById(saved.getId()).orElseGet(()->null);
        assertThat(found).isEqualTo(saved);
    }

    @Test
    @DisplayName("DeleteHistory Entity Update 테스트")
    void update() {
        final DeleteHistory saved = deleteHistoryRepository.save(D1);
        saved.setContentType(ContentType.ANSWER);
        final DeleteHistory found = deleteHistoryRepository.findById(saved.getId()).orElseThrow(() -> new RuntimeException("테스트실패"));
        assertThat(found.getContentType()).isEqualTo(ContentType.ANSWER);
    }

    @Test
    @DisplayName("DeleteHistory Entity Delete 테스트")
    void delete() {
        final DeleteHistory saved = deleteHistoryRepository.save(D1);
        deleteHistoryRepository.delete(saved);
        deleteHistoryRepository.flush();
        final DeleteHistory found = deleteHistoryRepository.findById(saved.getId()).orElseGet(() -> null);
        assertThat(found).isNull();
    }
}
