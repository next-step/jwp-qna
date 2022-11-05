package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.DeleteHistory;
import qna.domain.User;
import qna.fixture.TestDeleteHistoryFactory;
import qna.fixture.TestUserFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("삭제 이력을 저장할 수 있다")
    @Test
    void save() {
        User deleteBy = userRepository.save(TestUserFactory.create("서정국"));
        DeleteHistory expect = TestDeleteHistoryFactory.create(deleteBy);
        DeleteHistory result = deleteHistoryRepository.save(expect);

        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getContentId()).isEqualTo(expect.getContentId()),
                () -> assertThat(result.getDeletedBy()).isEqualTo(expect.getDeletedBy()),
                () -> assertThat(result.getCreateDate()).isEqualTo(expect.getCreateDate())
        );
    }
}
