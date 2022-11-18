package qna.study.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import qna.study.domain.eager.Class;

public interface ClassRepository extends JpaRepository<Class, Long> {

	@EntityGraph(attributePaths = "students")
	List<Class> findAll();

	@Query("select c from Class as c join fetch c.students")
	List<Class> findAllWithJpqlFetchJoin();
}
