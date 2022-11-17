package qna.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import qna.study.domain.eager.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
