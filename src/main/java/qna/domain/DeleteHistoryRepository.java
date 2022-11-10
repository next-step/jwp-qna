package qna.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeleteHistoryRepository extends JpaRepository<DeleteHistory, Long> {

    List<DeleteHistory> findAllByContentType(ContentType type);

    List<DeleteHistory> findAllByDeletedUser(User deletedUser);
}
