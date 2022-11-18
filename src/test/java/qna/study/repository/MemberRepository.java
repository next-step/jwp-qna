package qna.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import qna.study.domain.lazy.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
