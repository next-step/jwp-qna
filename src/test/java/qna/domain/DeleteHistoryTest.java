package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.fixture.DeleteHistoryFixture;
import qna.fixture.UserFixture;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("DeleteHistory 테스트")
class DeleteHistoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @DisplayName("Save 확인")
    @Test
    void save_확인() {
        // given
        User user = userRepository.save(UserFixture.create("user"));
        DeleteHistory deleteHistory = DeleteHistoryFixture.create(ContentType.ANSWER, 1L, user);

        // when
        DeleteHistory actual = deleteHistoryRepository.save(deleteHistory);

        // then
        assertThat(actual)
                .isEqualTo(deleteHistory);
    }

    @DisplayName("findById 확인")
    @Test
    void findById_확인() {
        // given
        User user = userRepository.save(UserFixture.create("user"));
        DeleteHistory question = DeleteHistoryFixture.create(ContentType.ANSWER, 1L, user);

        // when
        DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(question);
        Optional<DeleteHistory> actual = deleteHistoryRepository.findById(savedDeleteHistory.getId());

        // then
        assertThat(actual)
                .isPresent()
                .contains(savedDeleteHistory);
    }

    @DisplayName("update 확인")
    @Test
    void update_확인() {
        // given
        User user = userRepository.save(UserFixture.create("user"));
        DeleteHistory question = DeleteHistoryFixture.create(ContentType.ANSWER, 1L, user);

        // when
        DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(question);
        savedDeleteHistory.setContentType(ContentType.QUESTION);

        Optional<DeleteHistory> actual = deleteHistoryRepository.findById(savedDeleteHistory.getId());

        // then
        assertThat(actual)
                .isPresent();

        assertThat(actual.get().getContentType())
                .isEqualTo(ContentType.QUESTION);
    }
}