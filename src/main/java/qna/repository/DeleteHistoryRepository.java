package qna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import qna.domain.DeleteHistory;

public interface DeleteHistoryRepository extends JpaRepository<DeleteHistory, Long> {
}
