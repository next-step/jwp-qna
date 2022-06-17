package qna.feedback.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import qna.feedback.entity.eager.Class;

public interface ClassRepository extends JpaRepository<Class, Long> {

    @EntityGraph(attributePaths = "students")
    List<Class> findAll();

    @EntityGraph(attributePaths = "students")
    @Query("select distinct c from Class as c")
    List<Class> findAllByUpdatedAtIsNull();

    @Query("select c from Class as c join fetch c.students")
    List<Class> findAllWithJpqlFetchJoin();

    @Query("select distinct c from Class as c join fetch c.students")
    List<Class> findAllWithJpqlDistinctFetchJoin();
}
