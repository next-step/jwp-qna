package qna.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import qna.domain.DeleteHistory;
import qna.domain.User;

public interface DeleteHistoryRepository extends JpaRepository<DeleteHistory, Long> {
	List<DeleteHistory> findByDeletedUser(User deletedUser);
}
