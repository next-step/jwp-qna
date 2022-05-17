package qna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import qna.repository.entity.DeleteHistory;

public interface DeleteHistoryRepository extends JpaRepository<DeleteHistory, Long> {
}
