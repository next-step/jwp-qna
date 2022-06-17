package qna.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import qna.domain.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByDeletedFalse();

    Optional<Question> findByIdAndDeletedFalse(Long id);

    @Query(value = "select q from Question as q join fetch q.answers as a")
    List<Question> catationOperationQuery();

    @Query(value = "select distinct q from Question as q join fetch q.answers as a")
    List<Question> withDistinctQuery();

    @Query(
        value = "select distinct q from Question as q join fetch q.answers",
        countQuery = "select count(q) from Question as q"
    )
    List<Question> feedbackFetchJoinWithLimit(Pageable pageable);

    @Query(
        value = "select q from Question as q join fetch q.answers",
        countQuery = "select count(q) from Question as q"
    )
    List<Question> feedbackFetchJoinWithLimitAndDistinct(Pageable pageable);
}


//글로벌서비스 개발팀이 현재 운영하고 있는 서비스가 있는지
//    팀규모,
//제안 주신 두개 포지션 외 말씀드렸던 최근 합류한 다른 서비스 계열은 지원 불가능한지,
//
//
//    설명을 들어보고 고민해보면 어떨까 싶어 메신저 드렸습니다.
//    팀 소개를 해주신다고 말씀하셔서 제안 주신 두개 포지션에 대한 설명을 부탁드릴 수 있을까요?
//
//    팀 소개를 해주신다고 하셔서
//    제안주신 두개 포지션에 대한 설명을 들어보고 고민해보고 싶은데
//가능한지
//또한 두개 포지션 외 최근 합류한 다른 서비스 계열은 지원 불가능한지
//현재 운영하고 있는 서비스 및 팀규모, 기술스텍


