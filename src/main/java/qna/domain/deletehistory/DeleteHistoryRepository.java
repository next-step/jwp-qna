package qna.domain.deletehistory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeleteHistoryRepository extends JpaRepository<DeleteHistory, Long> {

	List<DeleteHistory> findDeleteHistoriesByContentType(ContentType contentType);
}
