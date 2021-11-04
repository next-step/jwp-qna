package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface DeleteHistoryRepository extends JpaRepository<DeleteHistory, Long> {
}
