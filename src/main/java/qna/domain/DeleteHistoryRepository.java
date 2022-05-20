package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface DeleteHistoryRepository extends JpaRepository<DeleteHistory, Long> {

    @Transactional
    default void saveAll(DeleteHistories deleteHistories) {
        saveAll(deleteHistories.get());
    }
}
