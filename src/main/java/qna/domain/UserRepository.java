package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import qna.domain.field.Name;
import qna.domain.field.UserId;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(UserId userId);

    /**
     * userId 조회를 통한 User 수 가져오기
     * @param userId
     * @return
     */
    long countByUserId(UserId userId);

    /**
     * 사용자 이름을 통한 User 조회
     * @param userName
     * @return
     */
    Optional<User> findByName(Name userName);

    /**
     * 사용자 이름을 통해 조회된 User 수 가져오기
     * @param userName
     * @return
     */
    long countByName(Name userName);

    /**
     * userId를 통해 사용자 정보 삭제 - 회원 탈퇴
     * @param userId
     */
    void deleteByUserId(UserId userId);
}
