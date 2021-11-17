package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeleteHistoryTest {

    public static final DeleteHistory D1 = new DeleteHistory(ContentType.QUESTION, UserTest.JAVAJIGI.getId(), UserTest.SANJIGI, LocalDateTime.now());

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

    @DisplayName("Create 및 ID 생성 테스트")
    @Test
    void save() {
        DeleteHistory save = deleteHistoryRepository.save(D1);
        assertThat(save.getId()).isNotNull();
    }

    @DisplayName("Read 테스트")
    @Test
    void read() {
        DeleteHistory save = deleteHistoryRepository.save(D1);
        DeleteHistory found = deleteHistoryRepository.findById(save.getId()).orElse(null);
        assertThat(found).isEqualTo(save);
    }

    @DisplayName("Update 테스트")
    @Test
    void update() {
        DeleteHistory save = deleteHistoryRepository.save(D1);
        save.setContentType(ContentType.ANSWER);
        DeleteHistory found = deleteHistoryRepository.findById(save.getId()).orElseThrow(() -> new NullPointerException("테스트실패"));
        assertThat(found.getContents()).isEqualTo(ContentType.ANSWER);
    }

    @DisplayName("Delete 테스트")
    @Test
    void delete() {
        DeleteHistory save = deleteHistoryRepository.save(D1);
        deleteHistoryRepository.delete(save);
        deleteHistoryRepository.flush();
        DeleteHistory found = deleteHistoryRepository.findById(save.getId()).orElse(null);
        assertThat(found).isNull();
    }
}
