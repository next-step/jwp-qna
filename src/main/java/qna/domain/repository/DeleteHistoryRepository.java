package qna.domain.repository;

import qna.domain.entity.DeleteHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeleteHistoryRepository extends JpaRepository<DeleteHistory, Long> {
}
