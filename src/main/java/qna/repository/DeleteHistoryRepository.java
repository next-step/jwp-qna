package qna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import qna.domain.DeleteHistory;
import qna.domain.User;

public interface DeleteHistoryRepository extends JpaRepository<DeleteHistory, Long> {

    DeleteHistory findByContentId(long contentId);

    DeleteHistory findByDeleter(User deleter);
}
