package qna.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeleteHistoryRepository extends JpaRepository<DeleteHistory, Long> {
    Optional<DeleteHistory> findByIdAndContentType(Long id, ContentType contentType);
}
