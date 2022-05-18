package qna.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import qna.question.domain.DeleteHistory;

public interface DeleteHistoryRepository extends JpaRepository<DeleteHistory, Long> {
}
