package subway.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByName(String name);
}
