package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeleteHistoryRepository extends JpaRepository<DeleteHistory, Long> {
    List<DeleteHistory> findByContentType(ContentType contentType);
}
