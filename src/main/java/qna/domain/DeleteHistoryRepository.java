package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DeleteHistoryRepository extends JpaRepository<DeleteHistory, Long> {
    Optional<DeleteHistory> findByContentTypeAndContentId(ContentType contentType, long contentId);

    List<DeleteHistory> findByCreateDateGreaterThanEqual(LocalDateTime createDate);

    List<DeleteHistory> findByCreateDateBetweenOrderByCreateDateDesc(LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<DeleteHistory> findByDeletedById(Long deletedById);
}
