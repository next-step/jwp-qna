package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static qna.enumType.ContentType.QUESTION;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import qna.domain.DeleteHistory;
import qna.domain.User;

@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@DataJpaTest
public class DeleteRepositoryTest {
    private static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    private static final DeleteHistory deleteHistoryTest = new DeleteHistory(QUESTION, 1L, JAVAJIGI);
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        userRepository.save(JAVAJIGI);
        deleteHistoryRepository.save(deleteHistoryTest);
    }

    @Test
    void findById() {
        DeleteHistory deleteHistory = deleteHistoryRepository.findById(1L).get();
        assertAll(
                () -> assertThat(deleteHistory.getContentType()).isEqualTo(QUESTION),
                () -> assertThat(deleteHistory.getContentId()).isEqualTo(1L),
                () -> assertThat(deleteHistory.getDeletedBy()).isEqualTo(1L)
        );
    }
}
