package qna.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeleteHistoryRepository extends JpaRepository<DeleteHistory, Long> {
    List<DeleteHistory> findByContentType(ContentType contentType);
}
