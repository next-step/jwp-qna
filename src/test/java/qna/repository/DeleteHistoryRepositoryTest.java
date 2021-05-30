package qna.repository;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.User;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @DisplayName("저장하기")
    @Test
    void save() {
        User deletedBy = new User("testUserId", "testPassword", "testName", "test@email.com");
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, deletedBy);

        DeleteHistory saveDeleteHistory = deleteHistoryRepository.save(deleteHistory);

        DeleteHistory findDeleteHistory = deleteHistoryRepository.findById(saveDeleteHistory.getId())
                .orElseThrow(() -> new IllegalStateException());

        assertThat(saveDeleteHistory).isEqualTo(findDeleteHistory);
        assertThat(saveDeleteHistory).isSameAs(findDeleteHistory);
    }

    @DisplayName("연관관계")
    @Test
    void relation() {
        User deletedBy = new User("testUserId", "testPassword", "testName", "test@email.com");
        userRepository.save(deletedBy);
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, deletedBy);
        DeleteHistory saveDeleteHistory = deleteHistoryRepository.save(deleteHistory);
        entityManager.flush();
        entityManager.clear();

        User findDeletedBy = deleteHistoryRepository.findById(saveDeleteHistory.getId())
                .orElseThrow(() -> new IllegalStateException()).getDeletedBy();

        assertThat(findDeletedBy.getUserId()).isEqualTo("testUserId");
    }
}
