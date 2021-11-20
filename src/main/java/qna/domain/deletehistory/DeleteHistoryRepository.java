package qna.domain.deletehistory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeleteHistoryRepository extends JpaRepository<DeleteHistory, Long> {

    List<DeleteHistory> findByDeleteTargetContentType(ContentType type);

    List<DeleteHistory> findByDeleteTarget(DeleteTarget deleteTarget);
}
