package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    private User deleteBy;

    @BeforeEach
    void setUp(@Autowired UserRepository userRepository) {
        deleteBy = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
    }

    @DisplayName("DeleteHistory 저장")
    @Test
    void save() {
        final DeleteHistory expected = deleteHistoryRepository.save(new DeleteHistory(ContentType.ANSWER, 1L, deleteBy, null));

        final Optional<DeleteHistory> actual = deleteHistoryRepository.findById(expected.getId());

        assertThat(actual).isPresent();
        assertThat(actual.get()).isSameAs(expected);
    }

}