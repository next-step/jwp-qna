package qna.question;

import org.springframework.data.jpa.repository.JpaRepository;
import qna.user.User;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByDeletedFalse();

    Optional<Question> findByIdAndDeletedFalse(Long id);

    List<Question> findByUser(User user);
}
