package qna.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeleteHistoryRepository extends JpaRepository<DeleteHistory, Long> {

	Optional<DeleteHistory> findById(Long id);
	List<DeleteHistory> findAll();
	List<DeleteHistory> findByDeletedById(Long id);

}
