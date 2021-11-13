package qna.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeleteHistoryRepository extends JpaRepository<DeleteHistory, Long> {
    Optional<DeleteHistory> findByContentId(Long contentId);

    Optional<DeleteHistory> findByDeletedById(Long deletedById);

    Optional<DeleteHistory> findByContentType(ContentType contentType);
}
