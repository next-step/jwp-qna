package qna.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeleteHistoryRepository extends JpaRepository<DeleteHistory, Long> {
	List<DeleteHistory> findByCreateDateBetween(LocalDateTime start, LocalDateTime end);
}
