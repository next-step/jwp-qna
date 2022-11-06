package qna.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import qna.domain.entity.DeleteHistory;

import java.util.Optional;

public interface DeleteHistoryRepository extends JpaRepository<DeleteHistory, Long> {
    Optional<DeleteHistory> findByContentId(Long contentId);
}
