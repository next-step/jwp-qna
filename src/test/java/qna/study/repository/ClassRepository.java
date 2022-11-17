package qna.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import qna.study.domain.eager.Class;

public interface ClassRepository extends JpaRepository<Class, Long> {
}
