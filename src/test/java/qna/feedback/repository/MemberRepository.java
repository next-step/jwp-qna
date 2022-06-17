package qna.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import qna.feedback.entity.lazy.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Modifying
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int increaseAgeOfAllMembersOver(@Param("age") int age);
}
