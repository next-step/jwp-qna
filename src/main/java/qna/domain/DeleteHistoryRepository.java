package qna.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeleteHistoryRepository extends JpaRepository<DeleteHistory, Long> {

	List<DeleteHistory> findAllByDeletedById(Long id);

}
