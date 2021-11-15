package qna.deletehistory;

import org.springframework.data.jpa.repository.JpaRepository;
import qna.domain.ContentType;

import java.util.List;

public interface DeleteHistoryRepository extends JpaRepository<DeleteHistory, Long> {
    List<DeleteHistory> findByContentType(ContentType contentType);
}
