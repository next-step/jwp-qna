package qna.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUserId(String userId);

	@Query(value = "SELECT userId FROM User WHERE email like %:emailPiece%")
	List<Object[]> findByEmail(@Param("emailPiece") String emailPiece);
}
