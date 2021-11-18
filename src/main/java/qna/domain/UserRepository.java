package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);

    /**
     * userId 조회를 통한 User 수 가져오기
     * @param userId
     * @return
     */
    long countByUserId(String userId);
}
