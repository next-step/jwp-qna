package qna.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import qna.feedback.entity.eager.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

}
