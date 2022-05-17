package qna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import qna.repository.entity.DeleteHistory;

@Repository
public interface DeleteHistoryRepository extends JpaRepository<DeleteHistory, Long> {
}
